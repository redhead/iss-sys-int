/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.service.OrderRepository;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Filip
 */
public class ExpeditionRouteTest extends OrderProcessRouteTest {

    @EndpointInject(uri = "direct:expedition")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:activemq")
    private MockEndpoint activemq;

    public ExpeditionRouteTest() {
    }

    @Test
    public void expeditionTest() throws Exception {

        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("activemqXa:*")
                        .skipSendToOriginalEndpoint()
                        .to("mock:activemq");
            }
        });
        activemq.setExpectedMessageCount(1);
        producer.sendBody(OrderRepository.get(1L));

        Thread.sleep(1000);

        activemq.assertIsSatisfied();

        assertTrue(activemq.getExchanges().get(0).getIn().getBody() instanceof String);
        assertEquals(activemq.getExchanges().get(0).getIn().getBody(), "{\"id\":1,\"address\":{\"firstName\":\"Jan\",\"lastName\":\"Novak\",\"street\":\"Purkynova\",\"city\":\"Brno\",\"zipCode\":\"61200\"},\"items\":[{\"articleId\":1,\"count\":2,\"unitPrice\":2.0},{\"articleId\":2,\"count\":2,\"unitPrice\":2.0}]}");

    }

    @Override
    protected String getTestedRouteName() {
        return "expedition";
    }

}
