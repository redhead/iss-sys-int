package cz.cvut.iss.sysint.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlTransient;

public class OrderItem {

    @JsonIgnore
    private String item;

    private int count;

    private double unitPrice;

    @XmlTransient
    @JsonIgnore
    private boolean fromSupplier;

    public OrderItem() {
    }

    public OrderItem(String item, int count, double unitPrice) {
        this.item = item;
        this.count = count;
        this.unitPrice = unitPrice;
    }

    @JsonIgnore
    public String getItem() {
        return item;
    }

    @JsonIgnore
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

    @JsonIgnore
    @XmlTransient
    public double getPrice() {
        return count * getUnitPrice();
    }

    @JsonIgnore
    public boolean isFromSupplier() {
        return fromSupplier;
    }

    @JsonIgnore
    public void setFromSupplier(boolean fromSupplier) {
        this.fromSupplier = fromSupplier;
    }

    @XmlTransient
    public int getArticleId() {
        return 1;
    }

    @Override
    public String toString() {
        return "OrderItem [articleId=" + item + ", count=" + count + ", unitPrice=" + unitPrice + ", fromSupplier=" + fromSupplier + "]";
    }
}
