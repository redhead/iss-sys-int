package cz.cvut.iss.sysint.accounting;

/**
 * @author Radek Jezdik
 */
public class AccountingOrderItem {

    private long articleId;

    private int count;

    private double unitPrice;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
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
