package taembe.example.blackwine.taembe.model.stores.coupon;

import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class SetCouponDataResponse {
    private CartResponse cart;
    private boolean ok;

    public CartResponse getCart() {
        return cart;
    }

    public void setCart(CartResponse cart) {
        this.cart = cart;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
