package cz.cvut.iss.sysint.service;

import cz.cvut.iss.sysint.exception.InvalidOrderException;
import cz.cvut.iss.sysint.model.Address;
import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderItem;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Radek Jezdik
 */
public class OrderValidator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = (Order) exchange.getIn().getBody();

        validate(order);

    }

    private void validate(Order order) {
        if (order.getId() != null || order.getStatus() != null) {
            throw new InvalidOrderException();
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new InvalidOrderException();
        }

        if (order.getAddress() == null) {
            throw new InvalidOrderException();
        }

        validate(order.getAddress());

        for (OrderItem item : order.getItems()) {
            validate(item);
        }
    }

    private void validate(OrderItem item) {
        if (item.getItem() == null || item.getItem().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (item.getCount() <= 0) {
            throw new InvalidOrderException();
        }
        if (item.getUnitPrice() <= 0) {
            throw new InvalidOrderException();
        }
    }

    private void validate(Address address) {
        if (address.getFirstName() == null || address.getFirstName().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (address.getLastName() == null || address.getLastName().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (address.getCity() == null || address.getCity().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            throw new InvalidOrderException();
        }
        if (address.getZipCode() == null || address.getZipCode().isEmpty()) {
            throw new InvalidOrderException();
        }
    }

}
