package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.model.Address;
import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderItem;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Radek Jezdik
 */
public class SoapToModelRequestConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Object message = ((MessageContentsList) exchange.getIn().getBody()).get(0);
        cz.cvut.iss.sysint.ws.Order soapOrder = (cz.cvut.iss.sysint.ws.Order) message;

        Order order = new Order();

        Address address = new Address();
        order.setAddress(address);

        address.setFirstName(soapOrder.getAddress().getFirstName());
        address.setLastName(soapOrder.getAddress().getLastName());
        address.setStreet(soapOrder.getAddress().getStreet());
        address.setCity(soapOrder.getAddress().getCity());
        address.setZipCode(soapOrder.getAddress().getZipCode());

        List<OrderItem> items = new ArrayList<OrderItem>();
        order.setItems(items);

        for (cz.cvut.iss.sysint.ws.OrderItem originalItem : soapOrder.getItems()) {
            OrderItem item = new OrderItem();
            item.setItem(originalItem.getItem());
            item.setUnitPrice(originalItem.getUnitPrice());
            item.setCount(originalItem.getCount());
            items.add(item);
        }

        exchange.getIn().setBody(order);
    }

}
