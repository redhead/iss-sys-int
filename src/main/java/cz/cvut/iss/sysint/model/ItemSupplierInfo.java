package cz.cvut.iss.sysint.model;

import java.math.BigDecimal;

/**
 * @author Radek Jezdik
 */
public class ItemSupplierInfo {

    protected boolean available;

    protected double price;

    public ItemSupplierInfo() {
    }

    public ItemSupplierInfo(boolean available, double price) {
        this.available = available;
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemSupplierInfo{" +
            "available=" + available +
            ", price=" + price +
            '}';
    }
}
