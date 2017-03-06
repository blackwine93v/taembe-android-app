package taembe.example.blackwine.taembe.common;

import java.util.List;

import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;
import taembe.example.blackwine.taembe.model.stores.cart.ItemCartModel;
import taembe.example.blackwine.taembe.model.stores.user.User;

/**
 * Created by Blackwine on 2/24/2017.
 */

public class MyGlobal {
    public static CartResponse myCart;
    public static int totalPrice = 0, finalTotalPrice = 0;
    public static String cookie = "";
    public static User user;
    public static final String BASE_URL = "https://taembe.vn/";

    public static String getPhoneNumber() {
        return PHONE_NUMBER;
    }

    public static final String
            PHONE_NUMBER = "0866546417";

    public static String getEmailTech() {
        return EMAIL_TECH;
    }

    public static final String EMAIL_TECH = "tech@taembe.com";

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyGlobal.user = user;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        MyGlobal.cookie = cookie;
    }

    public static void setTotalPrice(int totalPrice) {

        MyGlobal.totalPrice = totalPrice;
    }

    public static int getTotalPrice() {
        totalPrice = 0;
        List<ItemCartModel> items = CartResponse.parseItem(myCart.getItems());
        for(int i=0;i<items.size();i++){
            ItemCartModel item = items.get(i);
            totalPrice = totalPrice + item.getQty()*(item.getProduct().getSpecialPrice_integer()>0
                    ?item.getProduct().getSpecialPrice_integer()
                    :item.getProduct().getPrice_integer());
        }
        return totalPrice;
    }

    public static int getFinalTotalPrice(){
        int discountMount = 0;
        if(getMyCart().getListCoupon().size()>0)
            discountMount = getMyCart().getListCoupon().get(0).getChange();
        return getTotalPrice()+discountMount;
    }


    public static CartResponse getMyCart() {
        return myCart;
    }

    public static void setMyCart(CartResponse myCart) {
        MyGlobal.myCart = myCart;
    }
}
