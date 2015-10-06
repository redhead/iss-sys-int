package cz.cvut.iss.sysint.service;

import cz.cvut.iss.sysint.model.ItemSupplierInfo;
import cz.cvut.iss.sysint.model.OrderItem;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class SupplierOrderItemProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        OrderItem originalItem = (OrderItem) exchange.getProperties().get("originalItem");
        ItemSupplierInfo supplierInfo = (ItemSupplierInfo) exchange.getIn().getBody();

        originalItem.setFromSupplier(true);

        exchange.getIn().setBody(originalItem);
    }
}
