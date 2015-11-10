package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.converter.*;
import cz.cvut.iss.sysint.exception.CancelOrderException;
import cz.cvut.iss.sysint.model.Address;
import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderItem;
import cz.cvut.iss.sysint.service.*;
import exam.sysint.redhat.com.supplier_a.SupplierA;
import exam.sysint.redhat.com.supplier_b.SupplierB;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.component.http4.HttpOperationFailedException;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.soap.name.ServiceInterfaceStrategy;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //@formatter:off


        onException(CancelOrderException.class)
            .handled(true)
            .bean(OrderStatusProvider.class, "reservationNotPossible")
        .end();


        configRest();

        restEndpoint();
        soapEndpoint();

        from("direct:new-order")
            .id("newOrder")
            .process(new OrderValidator())
            .bean(OrderRepository.class, "create")
            .setProperty("orderId", simple("${body.id}"))
            .to("direct:process-order");


        from("direct:process-order")
            .id("orderProcess")
            .bean(OrderStatusProvider.class, "inProcess")
            .bean(Debugger.class,"process")
            .setProperty("isVipCustomer", method(VipCustomerProvider.class, "isVip"))
            .split(simple("${body.items}"))
                .to("direct:process-order-item")
            .end()
            .multicast()
                .to("direct:accounting")
                .to("activemqXa:shipping")
            .end();

        from("direct:process-order-item")
            .id("orderItemProcess")
            .setProperty("originalItem", body())
           	.to("direct:check-available")
           	.to("direct:best-choice")
            .end();
            
        
        from("direct:check-available")
        	.id("availabilityCheck")
        	.to("sql:select count from item where id=:#${property.originalItem.item}?dataSource=xaDataSource")
        	.process(new OrderItemFromSupplierProcessor())
        	.end();
        
        from("direct:best-choice")
        	.id("bestChoice")
        	.log("best choice ${body}")
        	.bean(Debugger.class, "process")
        	.choice()
            .when(header("fromSupplier").isEqualTo(true))
                .multicast()
                    .parallelProcessing()
                    .aggregationStrategy(new ChooseBestSupplierAggregationStrategy())
                    .to("direct:supplierA", "direct:supplierB")
                .end()
                .choice()
                    .when(body().isNull())
                        .log("Item not in inventory and no supplier has item available")
                        .throwException(new CancelOrderException())
                    .when(simple("${body.price} > ${exchangeProperty.originalItem.unitPrice} && ${exchangeProperty.isVipCustomer == false}"))
                        .log("Price is greater, but customer is not VIP")
                        .throwException(new CancelOrderException())
                    .otherwise()
                        .log("Marks the item as supplied from supplier")
                        .process(new SupplierOrderItemProcessor())
                .end()
            .end();
        	

        configureSupplierRoute(from("direct:supplierA"), "ref:supplierAHttp", SupplierA.class, new SupplierARequestConverter(), new SupplierAResponseConverter());
        configureSupplierRoute(from("direct:supplierB"), "ref:supplierBHttp", SupplierB.class, new SupplierBRequestConverter(), new SupplierBResponseConverter());

//        from("timer://foo?fixedRate=true&period=5000")
//        	.setProperty("articleId", constant("rhel"))
//        	.setHeader("articleId", constant("rhel"))
//            .to("sql:select count from item where id=':#${property.articleId}'?dataSource=xaDataSource")
//            .log("${body}");

        JacksonDataFormat json = new JacksonDataFormat();
        json.setEnableJaxbAnnotationModule(false);

        from("direct:accounting")
            .id("accounting")
            .process(new AccountingRequestConverter())
            .marshal(json)
            .bean(Debugger.class,"process")
            .log("${body}")
            .removeHeaders("*")
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
            .to("ref:accountingHttp")
            .convertBodyTo(String.class)
            .choice()
                .when(body().startsWith("{\"invoiceId\":-1"))
                    .log("Accounting reported INVALID invoice status")
                    .throwException(new CancelOrderException())
            .end();


//        from("timer://foo?fixedRate=true&period=10&repeatCount=1")
//            .setBody(method(this, "createOrder"))
//            .to("direct:new-order")
//            .log("${body}");

        //@formatter:on
    }

    public void configureSupplierRoute(RouteDefinition supplierRoute, String httpComponentName, Class<?> serviceInterface, Processor requestConverter, Processor responseConverter) {
        //@formatter:off
        SoapJaxbDataFormat soapDataFormat = createSoapDataFormat(serviceInterface);

        supplierRoute
            .onException(HttpOperationFailedException.class)
                .handled(true)
                .setBody(constant(null))
            .end()

            .process(requestConverter)
            .marshal(soapDataFormat)

            .removeHeaders("*")  // remove all headers so we don't send them in HTTP request
            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
            .to(httpComponentName)  // call endpoint referenced as supplierXHttp (makes HTTP request to web service)

            .unmarshal(soapDataFormat)
            .process(responseConverter);
        //@formatter:on
    }

    private SoapJaxbDataFormat createSoapDataFormat(Class<?> serviceInterface) {
        ServiceInterfaceStrategy elementNameStrategy = new ServiceInterfaceStrategy(serviceInterface, true);
        String pckgName = serviceInterface.getPackage().getName();

        return new SoapJaxbDataFormat(pckgName, elementNameStrategy);
    }

    public Object createOrder() {
        List<OrderItem> items = new ArrayList<>();
//        items.add(new OrderItem("fedora", 2, 1));
        items.add(new OrderItem("rhel", 200, 100));

        Order order = new Order();
        order.setAddress(new Address("c", "a", "a", "a", "a"));
        order.setItems(items);

        return order;
    }

    private void restEndpoint() {
        RestDefinition order = rest("/order")
            .consumes("application/json")
            .produces("application/json");

        order.post().type(Order.class)
            .to("direct:new-order");

        order.get()
            .to("direct:x");

        from("direct:x")
            .process(new Debugger())
            .setBody(simple("OK"));

//        order.get("/{orderId}")
//            .outType(Order.class)
//            .to("direct:find-order");
    }

    private void soapEndpoint() {
        // jetty proxy for SSL
        from("jetty:https://0.0.0.0:8444/services/OrderService?disableStreamCache=true&matchOnUriPrefix=true&sslContextParametersRef=#sslContextParameters&matchOnUriPrefix=true")
            .id("soapProxy")
//            .removeHeaders("Camel*")
            .to("jetty:http://localhost:8333/services/OrderService?bridgeEndpoint=true&throwExceptionOnFailure=false");

        from("cxf:bean:orderEndpoint")
            .id("soapEndpoint")
            .validate(body().isNotNull())
            .process(new SoapToModelRequestConverter())
            .to("direct:new-order");
    }

    private void configRest() {
        restConfiguration()
            .component("jetty")
            .bindingMode(RestBindingMode.json)
            .host("localhost")
            .scheme("https")
            .port(8888)

            .dataFormatProperty("prettyPrint", "true")
            .dataFormatProperty("include", "NON_NULL")
            .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES");
    }


    static class Debugger implements Processor {

        private String str;

        public Debugger() {
        }

        Debugger(String str) {
            this.str = str;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println(str);
        }
    }

}
