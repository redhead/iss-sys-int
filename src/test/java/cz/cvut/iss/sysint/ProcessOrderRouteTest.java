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
public class ProcessOrderRouteTest extends OrderProcessRouteTest {

    @EndpointInject(uri = "direct:process-order")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:process-order-item")
    private MockEndpoint processOrderItem;
    
    @EndpointInject(uri = "mock:accounting")
    private MockEndpoint accounting;
    
    @EndpointInject(uri = "mock:shipping")
    private MockEndpoint shipping;
    
    public ProcessOrderRouteTest() {
    }

    @Test
    public void testProcessOrder() throws Exception {
        
        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // intercept sending to direct:accounting and detour to our processor instead
                interceptSendToEndpoint("direct:process-order-item")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:process-order-item");
                interceptSendToEndpoint("direct:accounting")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:accounting");
                interceptSendToEndpoint("activemqXa:shipping")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:shipping");
            }
        });
        processOrderItem.expectedMessageCount(2);
        accounting.expectedMessageCount(1);
        shipping.expectedMessageCount(1);
        
        producer.sendBody(OrderRepository.get(1L));

        // wait a while to let the file be created
        Thread.sleep(2000);

        processOrderItem.assertIsSatisfied();
        accounting.assertIsSatisfied();
        shipping.assertIsSatisfied();
        
        assertEquals("Contacting Inventory, Accounting and Shipment", OrderRepository.get(1L).getStatus().getDescription());
        assertEquals("IN PROCESS", OrderRepository.get(1L).getStatus().getResolution());
    }

    @Override
    protected String getTestedRouteName() {
        return "orderProcess";
    }

}
