package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.model.OrderItem;
import exam.sysint.redhat.com.supplier_a.Available;
import exam.sysint.redhat.com.supplier_a.ItemRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class SupplierARequestConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OrderItem item = (OrderItem) exchange.getIn().getBody();
        item.getItem();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setSku(item.getItem());
        itemRequest.setAmount(item.getCount());

        Available available = new Available();
        available.setItemRequest(itemRequest);

        exchange.getIn().setBody(available);
    }

}
