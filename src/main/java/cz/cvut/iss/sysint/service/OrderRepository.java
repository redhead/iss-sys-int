package cz.cvut.iss.sysint.service;

import cz.cvut.iss.sysint.model.Order;
import cz.cvut.iss.sysint.model.OrderStatus;
import org.apache.camel.ExchangeProperty;

import java.util.Map;
import java.util.TreeMap;

public final class OrderRepository {

    private static final Map<Long, Order> ORDERS = new TreeMap<>();
    private static long sequence;

    public static void create(Order order) {
        order.setId(++sequence);
        ORDERS.put(order.getId(), order);
    }

    public static Order get(@ExchangeProperty("orderId") long id) {
        return ORDERS.get(id);
    }

    public static void clear() {
        ORDERS.clear();
        sequence = 0;
    }

    private OrderRepository() {
    }
}
