package cz.cvut.iss.sysint.ws;

/**
 * @author Radek Jezdik
 */
public class OrderItem {

    private String item;

    private int count;

    private double unitPrice;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
