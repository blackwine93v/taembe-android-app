package taembe.example.blackwine.taembe.model.stores.cart;

/**
 * Created by Blackwine on 2/22/2017.
 */

public class AddCartDataResponse {
    private String lastCartId;
    private boolean ok;
    private CartResponse cart;

    public static CartResponse getGlobalCart() {
        return globalCart;
    }

    public static void setGlobalCart(CartResponse globalCart) {
        AddCartDataResponse.globalCart = globalCart;
    }

    public static CartResponse globalCart;

    public String getLastCartId() {
        return lastCartId;
    }

    public void setLastCartId(String lastCartId) {
        this.lastCartId = lastCartId;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public CartResponse getCart() {
        return cart;
    }

    public void setCart(CartResponse cart) {
        this.cart = cart;
    }

    public AddCartDataResponse(String lastCartId, boolean ok, CartResponse cart) {

        this.lastCartId = lastCartId;
        this.ok = ok;
        this.cart = cart;
    }
}
