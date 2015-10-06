package cz.cvut.iss.sysint.service;

import cz.cvut.iss.sysint.model.Address;
import cz.cvut.iss.sysint.model.Order;
import org.apache.camel.Body;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Radek Jezdik
 */
public class VipCustomerProvider {

    private static Set<Address> vips = new HashSet<>();

    static {
        vips.add(new Address("a", "a", "a", "a", "a"));
        vips.add(new Address("b", "b", "b", "b", "b"));
    }

    public static boolean isVip(@Body Order order) {
        Address address = order.getAddress();
        return vips.contains(address);
    }

}
