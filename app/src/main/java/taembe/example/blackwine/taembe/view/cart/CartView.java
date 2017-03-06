package taembe.example.blackwine.taembe.view.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.adapter.RecyclerListItemCartAdapter;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.cart.AddCartDataResponse;
import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;
import taembe.example.blackwine.taembe.model.stores.cart.ItemCartModel;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.checkout.CheckoutView;

public class CartView extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<ItemCartModel> items = new ArrayList<>();
    private RecyclerListItemCartAdapter adapter = null;
    private Button btnCheckout;
    private TextView tvTotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        init();
    }

    private void loadListItem(){
        CartResponse cart = MyGlobal.getMyCart();
        items.clear();

        if(cart==null)
            return;
        if(cart.getItems().size()>0) {
            items.addAll(CartResponse.parseItem(cart.getItems()));
        }

        //
        tvTotalPrice.setText(String.valueOf(MyGlobal.getTotalPrice())+"đ");
        vLog.debug(TAG, "Reload cart data, number of item is "+items.size());
    }

    private void init(){
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListItemCartView);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Giỏ hàng của bạn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadListItem();
        if(adapter == null)
            adapter =  new RecyclerListItemCartAdapter(this, items);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size()==0){
                    Toast.makeText(CartView.this, "Không thể thanh toán khi giỏ hàng trống", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    return;
                }
                Intent checkoutView = new Intent(CartView.this, CheckoutView.class);
                startActivity(checkoutView);
            }
        });

        adapter.setOnClickItemEvent(new RecyclerListItemCartAdapter.setOnClickItem() {
            @Override
            public void onClickItemCart(ItemCartModel item) {
//                Intent viewProductDetail = new Intent(CartView.this, ProductDetail.class);
//                String productJson = new Gson().toJson(item.getProduct());
//                viewProductDetail.putExtra("PRODUCT_JSON", productJson);
//                startActivity(viewProductDetail);
            }

            @Override
            public void onClickButtonDelete(ItemCartModel item, final int position) {
                vLog.debug(TAG, "Xóa " + item.getKey());
                final String KEY = item.getKey().toString();
                JsonObject option = new JsonObject(), payload = new JsonObject();
                if(KEY.indexOf("=")>=0) {
                    option.addProperty(KEY.substring(KEY.indexOf(":")+1, KEY.indexOf("=")), KEY.substring(KEY.indexOf("=")+1));
                }
                payload.addProperty("is_absolute", true);
                payload.add("options", option);
                payload.addProperty("qty", 0);
                payload.addProperty("sku", item.getSku());
                vLog.debug(TAG, "Payload delete cart item " + new Gson().toJson(payload));

                ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
                Call<AddCartDataResponse> call = apiService.delItemInCart(payload);

                call.enqueue(new Callback<AddCartDataResponse>() {
                    @Override
                    public void onResponse(Call<AddCartDataResponse> call, Response<AddCartDataResponse> response) {
                        if(response.body()==null) {
                            Toast.makeText(CartView.this, "Xóa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!response.body().isOk()) {
                            Toast.makeText(CartView.this, "Xóa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(CartView.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        MyGlobal.setMyCart(response.body().getCart());
                        AddCartDataResponse data = response.body();
                        AddCartDataResponse.setGlobalCart(data.getCart());
                        loadListItem();
                        adapter.notifyDataSetChanged();
                        //adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailure(Call<AddCartDataResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onChangeQty(ItemCartModel item, int qty) {
                if(item.getQty()==qty)
                    return;
                vLog.debug(TAG, "Change qty " + item.getKey()+" from "+item.getQty()+" to "+qty);
                final String KEY = item.getKey().toString();
                JsonObject option = new JsonObject(), payload = new JsonObject();
                if(KEY.indexOf("=")>=0) {
                    if(KEY.indexOf(",")>=0) {
                        String option1 = KEY.substring(KEY.indexOf(":") + 1, KEY.indexOf(","));
                        String option2 = KEY.substring(KEY.indexOf(","));
                        option.addProperty(option1.substring(0, option1.indexOf("=")), option1.substring(option1.indexOf("=") + 1));
                        option.addProperty(option2.substring(1, option2.indexOf("=")), option2.substring(option2.indexOf("=") + 1));
                    }
                    else
                        option.addProperty(KEY.substring(KEY.indexOf(":")+1, KEY.indexOf("=")), KEY.substring(KEY.indexOf("=") + 1));

                }
                payload.addProperty("is_absolute", true);
                payload.add("options", option);
                payload.addProperty("qty", qty);
                payload.addProperty("sku", item.getSku());
                vLog.debug(TAG, "Payload edit cart item " + new Gson().toJson(payload));

                Call<AddCartDataResponse> call = APIClient.getClient().create(ApiInterface.class).editItemInCart(payload);
                call.enqueue(new Callback<AddCartDataResponse>() {
                    @Override
                    public void onResponse(Call<AddCartDataResponse> call, Response<AddCartDataResponse> response) {
                        if(response.body()==null) {
                            Toast.makeText(CartView.this, "Cập nhật sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!response.body().isOk()) {
                            Toast.makeText(CartView.this, "Cập nhật sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(CartView.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        MyGlobal.setMyCart(response.body().getCart());
                        AddCartDataResponse data = response.body();
                        AddCartDataResponse.setGlobalCart(data.getCart());
                        loadListItem();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<AddCartDataResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
