package taembe.example.blackwine.taembe.model.stores.cart;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import taembe.example.blackwine.taembe.model.stores.coupon.CouponResponse;
import taembe.example.blackwine.taembe.model.stores.product.Product;

/**
 * Created by Blackwine on 2/22/2017.
 */

public class CartResponse {
    private String created_at, updated_at, type;
    private JsonObject items;
    private List<CouponResponse> coupons = new ArrayList<>();

    public List<CouponResponse> getListCoupon() {
        return coupons;
    }

    public void setListCoupon(List<CouponResponse> listCoupon) {
        this.coupons = listCoupon;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonObject getItems() {
        if(items==null)
            return new JsonObject();
        return items;
    }

    public void setItems(JsonObject items) {
        this.items = items;
    }

    public static List<ItemCartModel> parseItem(JsonObject items) {
        if (items == null)
            return null;
        Set<Map.Entry<String, JsonElement>> set = items.entrySet();
        List<ItemCartModel> listItem = new ArrayList<>();
        for (Map.Entry entry : set) {
            final String KEY = entry.getKey().toString();
            ItemCartModel item = new Gson().fromJson(items.get(KEY), ItemCartModel.class);
            item.setKey(KEY);
            item.setSku(KEY.substring(0, KEY.indexOf(":")));
            listItem.add(item);
        }
        return listItem;
    }

    public List<Product> getListProduct(List<ItemCartModel> listItem){
        List<Product> products = new ArrayList<>();
        for(int i=0;i<listItem.size();i++){
            products.add(listItem.get(i).getProduct());
        }
        return products;
    }
}
