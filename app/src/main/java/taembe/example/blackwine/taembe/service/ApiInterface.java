package taembe.example.blackwine.taembe.service;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import taembe.example.blackwine.taembe.model.stores.home.HomeData;
import taembe.example.blackwine.taembe.model.stores.address.AddressDataResponse;
import taembe.example.blackwine.taembe.model.stores.cart.AddCartDataResponse;
import taembe.example.blackwine.taembe.model.stores.coupon.SetCouponDataResponse;
import taembe.example.blackwine.taembe.model.stores.product.GetProductSkuDataResponse;
import taembe.example.blackwine.taembe.model.stores.product.GetRelatedProductDataResponse;
import taembe.example.blackwine.taembe.model.stores.promotion.PromotionDataResponse;
import taembe.example.blackwine.taembe.model.stores.search.SearchDataResponse;
import taembe.example.blackwine.taembe.model.stores.user.User;

/**
 * Created by Blackwine on 2/21/2017.
 */

public interface ApiInterface {
    @GET("vi/?json=1")
    Call<HomeData> getHomeData();

    @GET("getpromo")
    Call<PromotionDataResponse> getAllPromotion();

    @GET("logout")
    Call<JsonObject> logout();

    @GET("login/status")
    Call<User> getStatus();

    @GET("appapi/version")
    Call<JsonObject> getLastestVersion();

    @GET("vi/{category_url}?json=1")
    Call<SearchDataResponse> getProductFromCategory(@Path("category_url") String category_url);

    @GET("vi/{category_url}")
    Call<SearchDataResponse> getProductFromCategory(@Path("category_url") String category_url, @QueryMap Map<String, String> options);

    @GET("appapi/region")
    Call<AddressDataResponse> getCitys();

    @GET("appapi/region/{city_id}")
    Call<AddressDataResponse> getDistricts(@Path("city_id") String city_id);

    @GET("appapi/region/{city_id}/{district_id}")
    Call<AddressDataResponse> getWards(@Path("city_id") String city_id, @Path("district_id") String district_id);

    @GET("vi/search")
    Call<SearchDataResponse> searchProduct(@QueryMap Map<String, String> options);

    @GET("customer_api/order/{lastCartId}")
    Call<JsonObject> getNumberOrder(@Path("lastCartId") String lastCartId);

    @GET("vi/product/{sku}?json=1")
    Call<GetProductSkuDataResponse> getProductBySku(@Path("sku") String sku);

    @GET("relatedproduct")
    Call<GetRelatedProductDataResponse> getRelatedProduct(@Query("name") String name);

    @POST("cart_api")
    Call<AddCartDataResponse> addCart(@Body JsonObject items);

    @POST("cart_api")
    Call<AddCartDataResponse> delItemInCart(@Body JsonObject items);

    @POST("cart_api")
    Call<AddCartDataResponse> editItemInCart(@Body JsonObject items);

    @POST("cart_api")
    Call<AddCartDataResponse> submitCheckout(@Body JsonObject items);

    @POST("coupon")
    Call<SetCouponDataResponse> setCoupon(@Body JsonObject items);

    @POST("login_account")
    Call<User> login(@Body JsonObject items);
}
