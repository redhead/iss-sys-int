package cz.cvut.iss.sysint.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7861726464876949615L;

	@XmlElement(nillable = true)
    private Long id;

    private Address address;

    private List<OrderItem> items;

    @JsonIgnore
    @XmlElement(nillable = true)
    private OrderStatus status;

    public Order() {
    }

    public Order(long id, List<OrderItem> items, Address address, OrderStatus status) {
        this.id = id;
        this.items = items;
        this.address = address;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + items + ", address=" + address + "]";
    }
}
