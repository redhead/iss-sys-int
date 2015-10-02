package cz.cvut.iss.sysint.model;

public class OrderItem {

    private String item;

    private int count;

    private double unitPrice;

    public OrderItem() {
    }

    public OrderItem(String item, int count, double unitPrice) {
        this.item = item;
        this.count = count;
        this.unitPrice = unitPrice;
    }

    public String getArticleId() {
        return item;
    }

    public void setArticleId(String item) {
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

    @Override
    public String toString() {
        return "OrderItem [articleId=" + articleId + ", count=" + count + ", unitPrice=" + unitPrice + "]";
    }
}
