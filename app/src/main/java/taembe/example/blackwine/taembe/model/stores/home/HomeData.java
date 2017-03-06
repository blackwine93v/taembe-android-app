package taembe.example.blackwine.taembe.model.stores.home;

import java.util.List;

import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;
import taembe.example.blackwine.taembe.model.stores.category.Category;
import taembe.example.blackwine.taembe.model.stores.product.Product;

/**
 * Created by Blackwine on 2/21/2017.
 */

public class HomeData {
    private List<Product> hotProducts;
    private List<Product> newProducts;
    private CartResponse cart;
    private List<Category> hotCategories;

    public List<Category> getHotCategories() {
        return hotCategories;
    }

    public void setHotCategories(List<Category> hotCategories) {
        this.hotCategories = hotCategories;
    }

    public CartResponse getCart() {
        return cart;
    }

    public void setCart(CartResponse cart) {
        this.cart = cart;
    }

    public List<Product> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(List<Product> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public List<Product> getNewProducts() {
        return newProducts;
    }

    public void setNewProducts(List<Product> newProducts) {
        this.newProducts = newProducts;
    }

    public HomeData(List<Product> hotProducts, List<Product> newProducts) {

        this.hotProducts = hotProducts;
        this.newProducts = newProducts;
    }
}
