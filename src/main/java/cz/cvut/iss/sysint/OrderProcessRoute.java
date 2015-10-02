package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;

public class OrderProcessRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        configRest();

        restEndpoint();
        soapEndpoint();

        from("direct:new-order")
            .log("${body}");

    }

    private void restEndpoint() {
        RestDefinition order = rest("/order")
            .consumes("application/json")
            .produces("application/json");

        order.post().type(Order.class)
            .to("direct:new-order");
//        order.get("/{orderId}")
//            .outType(Order.class)
//            .to("direct:find-order");
    }

    private void soapEndpoint() {
        // jetty proxy for SSL
        from("jetty:https://0.0.0.0:8443/services/OrderService?disableStreamCache=true&matchOnUriPrefix=true&sslContextParametersRef=#sslContextParameters&matchOnUriPrefix=true")
            .id("soapProxy")
            .removeHeaders("Camel*")
            .to("jetty:http://localhost:8333/services/OrderService?bridgeEndpoint=true&throwExceptionOnFailure=false");

        from("cxf:bean:orderEndpoint")
            .id("soapEndpoint")
            .validate(body().isNotNull())
            .convertBodyTo(Order.class)
            .to("direct:new-order");
    }

    private void configRest() {
        restConfiguration()
            .component("jetty")
            .bindingMode(RestBindingMode.json)
            .scheme("https")
            .port(8888)

            .dataFormatProperty("prettyPrint", "true")
            .dataFormatProperty("include", "NON_NULL")
            .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES");
    }


    static class Debugger implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println("Debug here");
        }
    }

}
