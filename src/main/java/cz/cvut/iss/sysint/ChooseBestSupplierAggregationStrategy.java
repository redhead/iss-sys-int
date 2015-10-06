package cz.cvut.iss.sysint;

import cz.cvut.iss.sysint.model.ItemSupplierInfo;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.math.BigDecimal;

/**
 * @author Radek Jezdik
 */
public class ChooseBestSupplierAggregationStrategy implements AggregationStrategy {


    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        ItemSupplierInfo oldInfo = (oldExchange == null ? null : (ItemSupplierInfo) oldExchange.getIn().getBody());
        ItemSupplierInfo newInfo = (ItemSupplierInfo) newExchange.getIn().getBody();

        if (newInfo != null && newInfo.isAvailable() && hasBetterPriceThanOldInfo(newInfo, oldInfo)) {
            return newExchange;

        } else {
            if (oldExchange == null) {
                newExchange.getIn().setBody(null);
                return newExchange;
            } else {
                return oldExchange;
            }
        }
    }

    private boolean hasBetterPriceThanOldInfo(ItemSupplierInfo newInfo, ItemSupplierInfo oldInfo) {
        if (oldInfo == null) {
            return true;
        }

        double newPrice = newInfo.getPrice();
        double oldPrice = oldInfo.getPrice();

        return newPrice < oldPrice;
    }

}
