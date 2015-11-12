/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.exception.CancelOrderException;
import cz.cvut.iss.sysint.model.ItemSupplierInfo;
import cz.cvut.iss.sysint.model.OrderItem;
import cz.cvut.iss.sysint.service.OrderRepository;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Filip
 */
public class BestChoiceRouteTest extends OrderProcessRouteTest {

    @EndpointInject(uri = "direct:best-choice")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:supplierA")
    private MockEndpoint supplierA;
    
    @EndpointInject(uri = "mock:supplierB")
    private MockEndpoint supplierB;
    
    @EndpointInject(uri = "mock:exception")
    private MockEndpoint exception;
    
    public BestChoiceRouteTest() {
    }

    @Test
    public void itemNotAvailableTest() throws Exception {
        
        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // intercept sending to direct:accounting and detour to our processor instead
                interceptSendToEndpoint("direct:supplierA")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:supplierA");
                interceptSendToEndpoint("direct:supplierB")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .to("mock:supplierB");
                onException(CancelOrderException.class)
                        .log("CancelOrderException")
                        .handled(true)
                        .to("mock:exception");
            }
        });
        
       
        
        exception.setExpectedMessageCount(1);
        
        producer.sendBodyAndHeader(null, "fromSupplier", true);

        // wait a while to let the file be created
        Thread.sleep(2000);

        exception.assertIsSatisfied();
        exception.reset();
    }

    @Test
    public void differentPriceTest() throws Exception {
        
        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // intercept sending to direct:accounting and detour to our processor instead
                interceptSendToEndpoint("direct:supplierA")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .process(exchange -> {
                        ItemSupplierInfo mockResponse = new ItemSupplierInfo(true, 10.0);
                        exchange.getOut().setBody(mockResponse);
                    });
                interceptSendToEndpoint("direct:supplierB")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .process(exchange -> {
                        ItemSupplierInfo mockResponse = new ItemSupplierInfo(true, 10.0);
                        exchange.getOut().setBody(mockResponse);
                    });
                onException(CancelOrderException.class)
                        .log("CancelOrderException")
                        .handled(true)
                        .to("mock:exception");
                
            }
        });
        
       
        Exchange exchange = new DefaultExchange(context);
        exchange.setProperty("originalItem", OrderRepository.get(1L).getItems().get(0));
        exchange.setProperty("isVipCustomer", false);
        
        exchange.getIn().setBody(new ItemSupplierInfo(true, 2000.0));
        exchange.getIn().setHeader("fromSupplier", true);
        exception.setExpectedMessageCount(1);
        
        producer.send(exchange);

        // wait a while to let the file be created
        Thread.sleep(2000);

        exception.assertIsSatisfied();
        exception.reset();
    }
    
        @Test
    public void itemSuppliedBySupplierTest() throws Exception {
        
        RouteDefinition route = context.getRouteDefinitions().get(0);
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // intercept sending to direct:accounting and detour to our processor instead
                interceptSendToEndpoint("direct:supplierA")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .process(exchange -> {
                        ItemSupplierInfo mockResponse = new ItemSupplierInfo(true, 10.0);
                        exchange.getOut().setBody(mockResponse);
                    });
                interceptSendToEndpoint("direct:supplierB")
                    // skip sending to the real http when the detour ends
                    .skipSendToOriginalEndpoint()
                    .process(exchange -> {
                        ItemSupplierInfo mockResponse = new ItemSupplierInfo(true, 10.0);
                        exchange.getOut().setBody(mockResponse);
                    });
                onException(CancelOrderException.class)
                        .log("CancelOrderException")
                        .handled(true)
                        .to("mock:exception");
            }
        });
        
       
        
        Exchange exchange = new DefaultExchange(context);
        exchange.setProperty("originalItem", OrderRepository.get(1L).getItems().get(0));
        exchange.setProperty("isVipCustomer", true);
        
        exchange.getIn().setBody(new ItemSupplierInfo(true, 2000.0));
        exchange.getIn().setHeader("fromSupplier", true);
        
        producer.send(exchange);

        // wait a while to let the file be created
        Thread.sleep(2000);

        assertTrue(exchange.getIn().getBody() instanceof OrderItem);
        exception.reset();
    }
    
    @Override
    protected String getTestedRouteName() {
        return "bestChoice";
    }

}
