package cz.cvut.iss.sysint.ws;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author Radek Jezdik
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    @XmlElement
    private Address address;

    @XmlElement
    private List<OrderItem> items;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
