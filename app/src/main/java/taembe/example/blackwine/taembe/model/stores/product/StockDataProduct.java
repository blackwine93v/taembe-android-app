package taembe.example.blackwine.taembe.model.stores.product;

/**
 * Created by Blackwine on 2/21/2017.
 */

public class StockDataProduct {
    private String is_in_stock, qty;

    public String getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(String is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public String getQty() {
        return qty;
    }

    public int getQtyInteger() {
        return Math.round(Float.parseFloat(qty));
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public StockDataProduct(String is_in_stock, String qty) {

        this.is_in_stock = is_in_stock;
        this.qty = qty;
    }
}
