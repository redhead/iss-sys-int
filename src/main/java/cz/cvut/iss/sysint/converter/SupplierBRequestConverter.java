package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.model.OrderItem;
import exam.sysint.redhat.com.supplier_b.ItemRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class SupplierBRequestConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OrderItem item = (OrderItem) exchange.getIn().getBody();
        item.getItem();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setSku(item.getItem());
        itemRequest.setAmount(item.getCount());

        exchange.getIn().setBody(itemRequest);
    }

}
