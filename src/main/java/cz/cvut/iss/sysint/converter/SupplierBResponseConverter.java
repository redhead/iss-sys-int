package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.model.ItemSupplierInfo;
import exam.sysint.redhat.com.supplier_b.ItemReply;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class SupplierBResponseConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        ItemReply itemReply = (ItemReply) exchange.getIn().getBody();

        ItemSupplierInfo itemSupplierInfo = new ItemSupplierInfo(itemReply.isAvailable(), itemReply.getPrice().doubleValue());
        exchange.getIn().setBody(itemSupplierInfo);
    }

}
