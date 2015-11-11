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
public class ProcessOrderItemRouteTest extends OrderProcessRouteTest {

    @EndpointInject(uri = "direct:process-order-item")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:check-available")
    private MockEndpoint checkAvailable;
    
    @EndpointInject(uri = "mock:best-choice")
    private MockEndpoint bestChoice;
    
    public ProcessOrderItemRouteTest() {
    }

    @Test
    public void testProcessOrderItem() throws Exception {
        
        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // intercept sending to direct:accounting and detour to our processor instead
                interceptSendToEndpoint("direct:check-available")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:check-available");
                interceptSendToEndpoint("direct:best-choice")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:best-choice");
            }
        });
        checkAvailable.expectedMessageCount(1);
        bestChoice.expectedMessageCount(1);
        
        producer.sendBody(OrderRepository.get(1L).getItems().get(0));

        // wait a while to let the file be created
        Thread.sleep(2000);

        checkAvailable.assertIsSatisfied();
        bestChoice.assertIsSatisfied();
    }

    @Override
    protected String getTestedRouteName() {
        return "orderItemProcess";
    }

}
