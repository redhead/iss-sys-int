package cz.cvut.iss.sysint.converter;

import cz.cvut.iss.sysint.accounting.AccountingOrder;
import cz.cvut.iss.sysint.accounting.AccountingOrderItem;
import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderItem;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Radek Jezdik
 */
public class AccountingRequestConverter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Order originalOrder = (Order) exchange.getIn().getBody();

        AccountingOrder order = new AccountingOrder();
        order.setId(originalOrder.getId());
        order.setAddress(originalOrder.getAddress());

        List<AccountingOrderItem> items = new ArrayList<AccountingOrderItem>();
        order.setItems(items);

        int num = 1;

        for (OrderItem originalItem : originalOrder.getItems()) {
            AccountingOrderItem item = new AccountingOrderItem();
            item.setArticleId(num++);
            item.setUnitPrice(originalItem.getUnitPrice());
            item.setCount(originalItem.getCount());
            items.add(item);
        }

        exchange.getIn().setBody(order);
    }

}
