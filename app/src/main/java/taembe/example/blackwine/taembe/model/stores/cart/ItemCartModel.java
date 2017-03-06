package taembe.example.blackwine.taembe.model.stores.cart;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import taembe.example.blackwine.taembe.model.stores.product.Product;

/**
 * Created by Blackwine on 2/23/2017.
 */

public class ItemCartModel {
    private String key, sku;
    private List<JsonObject> options = new ArrayList<>();
    private int qty;
    private Product product;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemCartModel(String key, String sku, List<JsonObject> options, int qty, Product product) {
        this.key = key;
        this.sku = sku;
        this.options = options;
        this.qty = qty;
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<JsonObject> getOptions() {
        return options;
    }

    public void setOptions(List<JsonObject> options) {
        this.options = options;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
