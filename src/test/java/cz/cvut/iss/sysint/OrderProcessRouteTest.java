package cz.cvut.iss.sysint;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;

import cz.cvut.iss.sysint.model.Address;
import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderItem;
import cz.cvut.iss.sysint.service.OrderRepository;

public abstract class OrderProcessRouteTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("https4", context.getComponent("mock"));
        context.addComponent("activemq", context.getComponent("seda"));

        Properties initialProperties = new Properties();
        initialProperties.put("endpoint.file.baseUrl", "./");
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setInitialProperties(initialProperties);

        RouteBuilder builder = new OrderProcessRoute();
        builder.setContext(context);
        builder.configure();
        
        RouteDefinition route = builder.getRouteCollection().getRoutes().stream()
            .filter(r -> r.getId().equals(getTestedRouteName()))
            .findFirst()
            .get();
        context.addRouteDefinition(route);

        return context;
    }

    @Before
    public void createOrder() {
        List<OrderItem> items = new ArrayList<>(2);
        items.add(new OrderItem("item1", 2, 2.0));
        items.add(new OrderItem("item2", 2, 2.0));

        Address address = new Address("Jan", "Novak", "Purkynova", "Brno", "61200");

        Order order = new Order(1L, items, address, null);

        OrderRepository.clear();
        OrderRepository.create(order); // ID 1
    }

    protected abstract String getTestedRouteName();
}
