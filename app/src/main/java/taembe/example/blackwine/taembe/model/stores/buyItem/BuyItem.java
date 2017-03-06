package taembe.example.blackwine.taembe.model.stores.buyItem;

import com.google.gson.JsonObject;

/**
 * Created by Blackwine on 2/22/2017.
 */

public class BuyItem {
    private String sku;
    private int qty = 1;
    private JsonObject options = new JsonObject();



    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public JsonObject getOptions() {
        return options;
    }

    public void setOptions(JsonObject options) {
        this.options = options;
    }

    public BuyItem(String sku, int qty, JsonObject options) {
        this.sku = sku;
        this.qty = qty;
        this.options = options;
    }
    public BuyItem(String sku, int qty) {
        this.sku = sku;
        this.qty = qty;
    }
}
