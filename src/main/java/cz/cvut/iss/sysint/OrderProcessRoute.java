package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.model.Order;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestDefinition;

public class OrderProcessRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("restlet")
            .bindingMode(RestBindingMode.json)
            .port(8888)

            .dataFormatProperty("prettyPrint", "true")
            .dataFormatProperty("include", "NON_NULL") //
            .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES");

        RestDefinition order = rest("/order")
            .consumes("application/json")
            .produces("application/json");

        order.post().type(Order.class)
            .to("direct:new-order");
//        order.get("/{orderId}").outType(Order.class).to("direct:find-order");

        from("direct:new-order")
            .log("${body}");

    }
}
