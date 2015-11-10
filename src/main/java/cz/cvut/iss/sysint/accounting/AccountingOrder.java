package cz.cvut.iss.sysint.accounting;

import cz.cvut.iss.sysint.model.Address;

import java.util.List;

/**
 * @author Radek Jezdik
 */
public class AccountingOrder {

    private Long id;

    private Address address;

    private List<AccountingOrderItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<AccountingOrderItem> getItems() {
        return items;
    }

    public void setItems(List<AccountingOrderItem> items) {
        this.items = items;
    }
}
