package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.model.ItemSupplierInfo;
import exam.sysint.redhat.com.supplier_a.AvailableResponse;
import exam.sysint.redhat.com.supplier_a.ItemReply;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class SupplierAResponseConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        AvailableResponse response = (AvailableResponse) exchange.getIn().getBody();
        ItemReply itemReply = response.getItemReply();

        ItemSupplierInfo itemSupplierInfo = new ItemSupplierInfo(itemReply.isAvailable(), itemReply.getPrice().doubleValue());
        exchange.getIn().setBody(itemSupplierInfo);
    }

}
