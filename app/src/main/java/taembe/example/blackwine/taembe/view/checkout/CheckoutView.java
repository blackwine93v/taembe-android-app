package taembe.example.blackwine.taembe.view.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.address.Address;
import taembe.example.blackwine.taembe.model.stores.address.AddressDataResponse;
import taembe.example.blackwine.taembe.model.stores.cart.AddCartDataResponse;
import taembe.example.blackwine.taembe.model.stores.coupon.SetCouponDataResponse;
import taembe.example.blackwine.taembe.model.stores.customer.CustomerForCheckout;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.thankyou.ThankYouView;

public class CheckoutView extends AppCompatActivity implements View.OnClickListener{
    private static final int DEFAULT_CITY_POSITION = 44;
    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    Button btnSubmitCheckout, btnApplyCouponCheckout;
    EditText editTextYournameCheckout, editTextEmailCheckout,editTextYourphoneCheckout,
            editTextStreetCheckout, editTextNoteCheckout, editTextCoupon;
    TextView tvFeeShipCheckout, tvDisountCheckout, tvFinalTotalCheckout;
    Spinner spinnerCountryCheckout, spinnerDistrictCheckout,spinnerWardCheckout;
    private Address city, district, ward;
    private final int CITY_ADDR_TYPE = 1, DISTICT_ADDR_TYPE = 2, WARD_ADDR_TYPE =3;
    private int feeDelivery, threshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_view);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        editTextYournameCheckout = (EditText) findViewById(R.id.editTextYournameCheckout);
        editTextEmailCheckout = (EditText) findViewById(R.id.editTextEmailCheckout);
        editTextYourphoneCheckout = (EditText) findViewById(R.id.editTextYourphoneCheckout);
        editTextStreetCheckout = (EditText) findViewById(R.id.editTextStreetCheckout);
        editTextNoteCheckout = (EditText) findViewById(R.id.editTextNoteCheckout);
        editTextCoupon = (EditText) findViewById(R.id.editTextCoupon);
        btnSubmitCheckout = (Button) findViewById(R.id.btnSubmitCheckout);
        btnApplyCouponCheckout = (Button) findViewById(R.id.btnApplyCouponCheckout);
        tvFeeShipCheckout = (TextView) findViewById(R.id.tvFeeShipCheckout);
        tvDisountCheckout = (TextView) findViewById(R.id.tvDisountCheckout);
        tvFinalTotalCheckout = (TextView) findViewById(R.id.tvFinalTotalCheckout);
        spinnerCountryCheckout = (Spinner) findViewById(R.id.spinnerCountryCheckout);
        spinnerDistrictCheckout = (Spinner) findViewById(R.id.spinnerDistrictCheckout);
        spinnerWardCheckout = (Spinner) findViewById(R.id.spinnerWardCheckout);

        btnApplyCouponCheckout.setOnClickListener(this);
        btnSubmitCheckout.setOnClickListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thanh toán");
        loadSumary();
        loadRegion(CITY_ADDR_TYPE);
    }

    private void loadRegion(final int type){
        Call<AddressDataResponse> call = null;
        if(type == CITY_ADDR_TYPE) {
            call = APIClient.getClient().create(ApiInterface.class).getCitys();
        }else if(type == DISTICT_ADDR_TYPE) {
            call = APIClient.getClient().create(ApiInterface.class).getDistricts(city.getCode());
        }else if(type == WARD_ADDR_TYPE){
            call = APIClient.getClient().create(ApiInterface.class).getWards(city.getCode(), district.getCode());
        }

        call.enqueue(new Callback<AddressDataResponse>() {
            @Override
            public void onResponse(Call<AddressDataResponse> call, Response<AddressDataResponse> response) {
                if(response.errorBody()!=null || response.body()==null){
                    Toast.makeText(CheckoutView.this, "Lỗi khi lấy danh sách địa chỉ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(response.body()!=null){
                    setAddressSelector(response.body().getRegion(), type);
                }
            }
            @Override
            public void onFailure(Call<AddressDataResponse> call, Throwable t) {

            }
        });
    }

    private void setAddressSelector(final List<Address>addresses, final int type) {
        Spinner spinner = null;
        if(type == CITY_ADDR_TYPE) {
            spinner = spinnerCountryCheckout;
        }else if(type == DISTICT_ADDR_TYPE) {
            spinner = spinnerDistrictCheckout;
        }else if(type == WARD_ADDR_TYPE){
            spinner = spinnerWardCheckout;
        }

        String[] lists = new String[addresses.size()];
        for(int i=0;i<addresses.size();i++){
            lists[i] = addresses.get(i).getName();
        }
        final ArrayAdapter adapter = new ArrayAdapter(CheckoutView.this, R.layout.support_simple_spinner_dropdown_item, lists);
        spinner.setAdapter(adapter);

        if(type==CITY_ADDR_TYPE){
            spinner.setSelection(DEFAULT_CITY_POSITION);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handlerAddressSelected(addresses.get(position), type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handlerAddressSelected(Address address, int type) {
        if(type== CITY_ADDR_TYPE){
            city = address;
            loadRegion(DISTICT_ADDR_TYPE);
        }
        else if(type == DISTICT_ADDR_TYPE){
            district = address;
            loadRegion(WARD_ADDR_TYPE);
        }
        else if(type == WARD_ADDR_TYPE){
            ward = address;
            computeFeeDelivery();
        }
    }

    private void computeFeeDelivery() {
        if(ward!=null && ward.getDelivery()!=null && ward.getDelivery().getFee()>0){
            feeDelivery = ward.getDelivery().getFee();
            threshold = ward.getDelivery().getThreshold();
        }
        else if(district!=null && district.getDelivery()!=null && district.getDelivery().getFee()>0){
            feeDelivery = district.getDelivery().getFee();
            threshold = district.getDelivery().getThreshold();
        }else if(city!=null && city.getDelivery()!=null && city.getDelivery().getFee()>0){
            feeDelivery = city.getDelivery().getFee();
            threshold = city.getDelivery().getThreshold();
        }
        tvFeeShipCheckout.setText(String.valueOf(feeDelivery) + getString(R.string.money_unit));
        tvFinalTotalCheckout.setText((MyGlobal.getFinalTotalPrice()+feeDelivery + getString(R.string.money_unit)));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmitCheckout:
                submitCheckout();
                break;
            case R.id.btnApplyCouponCheckout:
                setCoupon(editTextCoupon.getText().toString());
                break;
        }
    }

    private void setCoupon(String couponString) {
        if(couponString==null) {
            Toast.makeText(CheckoutView.this, "Mã giảm giá không họp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        couponString = couponString.replace(" ","");
        vLog.debug(TAG, "Coupon code is "+couponString + " Length: "+couponString.length());

        JsonObject payload = new JsonObject();
        payload.addProperty("discountCodeString", couponString);
        Call<SetCouponDataResponse> call = APIClient.getClient().create(ApiInterface.class).setCoupon(payload);
        call.enqueue(new Callback<SetCouponDataResponse>() {
            @Override
            public void onResponse(Call<SetCouponDataResponse> call, Response<SetCouponDataResponse> response) {
                if(response.errorBody()!=null){
                    int codeResponse = response.code();
                    if(codeResponse == 403)
                        Toast.makeText(CheckoutView.this, "Hiện tại không thể sử dụng mã giảm giá này (#403)", Toast.LENGTH_SHORT).show();
                    if(codeResponse == 401)
                        Toast.makeText(CheckoutView.this, "Bạn phải đăng nhập để sử dụng mã này (#401)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body() == null ){
                    Toast.makeText(CheckoutView.this, "Lỗi khi áp dụng mã giảm giá, xin thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body().isOk()){
                    MyGlobal.setMyCart(response.body().getCart());
                    loadSumary();
                }
            }

            @Override
            public void onFailure(Call<SetCouponDataResponse> call, Throwable t) {

            }
        });
    }

    private void loadSumary() {
        if(MyGlobal.getMyCart()!=null && MyGlobal.getMyCart().getListCoupon().size()>0)
            tvDisountCheckout.setText(MyGlobal.getMyCart().getListCoupon().get(0).getChange()+ getString(R.string.money_unit));
        tvFinalTotalCheckout.setText(MyGlobal.getFinalTotalPrice()+ getString(R.string.money_unit));
    }

    private void submitCheckout() {
        CustomerForCheckout customer = new CustomerForCheckout();
        customer.setFirstname(editTextYournameCheckout.getText().toString());
        customer.setComment(editTextNoteCheckout.getText().toString());
        customer.setCurrentPaymentMethod("cod");
        customer.setEmail(editTextEmailCheckout.getText().toString());
        customer.setStreet(editTextStreetCheckout.getText().toString());
        customer.setTelephone(editTextYourphoneCheckout.getText().toString());

        JsonObject payload = new JsonObject();

        payload.add("customer", new Gson().toJsonTree(customer));
        payload.size();

        Call<AddCartDataResponse> call = APIClient.getClient().create(ApiInterface.class).submitCheckout(payload);
        call.enqueue(new Callback<AddCartDataResponse>() {
            @Override
            public void onResponse(Call<AddCartDataResponse> call, Response<AddCartDataResponse> response) {
                AddCartDataResponse addCartDataResponse = response.body();
                if(addCartDataResponse == null || !addCartDataResponse.isOk()){
                    Toast.makeText(CheckoutView.this, "Lỗi: Thanh toán không thành công, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Set cart to global /reset cart to empty
                MyGlobal.setMyCart(addCartDataResponse.getCart());
                Intent thankyouView = new Intent(CheckoutView.this, ThankYouView.class);
                thankyouView.putExtra("lastCartId", addCartDataResponse.getLastCartId());
                startActivity(thankyouView);
            }

            @Override
            public void onFailure(Call<AddCartDataResponse> call, Throwable t) {

            }
        });
    }
}
