package cz.cvut.iss.sysint.service;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public final class OrderItemFromSupplierProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		List<Map<String,Integer>> rows=exchange.getIn().getBody(List.class);
		Map<String, Integer> row=rows.get(0);
	    System.out.println(row.get("COUNT"));
	    int itemAvailability=Integer.valueOf(row.get("COUNT"));
		exchange.getIn().setHeader("fromSupplier", itemAvailability<=0);
	}
}