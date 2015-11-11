package cz.cvut.iss.sysint.model;

import java.io.Serializable;

public class OrderItem implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1489342672666255095L;

	private String item;

    private int count;

    private double unitPrice;

    private boolean fromSupplier;

    public OrderItem() {
    }

    public OrderItem(String item, int count, double unitPrice) {
        this.item = item;
        this.count = count;
        this.unitPrice = unitPrice;
    }

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

    public double getPrice() {
        return count * getUnitPrice();
    }

    public boolean isFromSupplier() {
        return fromSupplier;
    }

    public void setFromSupplier(boolean fromSupplier) {
        this.fromSupplier = fromSupplier;
    }

    @Override
    public String toString() {
        return "OrderItem [articleId=" + item + ", count=" + count + ", unitPrice=" + unitPrice + ", fromSupplier=" + fromSupplier + "]";
    }
}
