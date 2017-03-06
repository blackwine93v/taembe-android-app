package taembe.example.blackwine.taembe.view.product;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.product.AdditionalOption;
import taembe.example.blackwine.taembe.model.stores.product.Image;
import taembe.example.blackwine.taembe.model.stores.product.OptionProduct;
import taembe.example.blackwine.taembe.model.stores.buyItem.BuyItem;
import taembe.example.blackwine.taembe.model.stores.cart.AddCartDataResponse;
import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;
import taembe.example.blackwine.taembe.model.stores.product.GetRelatedProductDataResponse;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.cart.CartView;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    TextView tvTitleProductDetail, tvPriceProductDetail, tvDescriptionProductDetail,
            tvSkuProductDetail, tvMadeinProductDetail, tvBrandProductDetail,tvStatusProductDetail,
            tvOldPriceProductDetail;
    ImageView ivImageProductDetail;
    FrameLayout frameLayoutOptionProductDetail, frameLayoutImagesProductDetail;
    Button btnBuyProductDetail;

    private boolean optionRequire = false;

    //Creating buy item package
    private BuyItem buyItem = null;
    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        init();
        loadData();
    }


    private void init(){
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        tvTitleProductDetail = (TextView) findViewById(R.id.tvTitleProductDetail);
        tvDescriptionProductDetail = (TextView) findViewById(R.id.tvDescriptionProductDetail);
        tvSkuProductDetail = (TextView) findViewById(R.id.tvSkuProductDetail);
        tvMadeinProductDetail = (TextView) findViewById(R.id.tvMadeinProductDetail);
        tvBrandProductDetail = (TextView) findViewById(R.id.tvBrandProductDetail);
        tvPriceProductDetail = (TextView) findViewById(R.id.tvPriceProductDetail);
        tvOldPriceProductDetail = (TextView) findViewById(R.id.tvOldPriceProductDetail);
        tvStatusProductDetail = (TextView) findViewById(R.id.tvStatusProductDetail);
        ivImageProductDetail = (ImageView) findViewById(R.id.ivImageProductDetail);
        frameLayoutOptionProductDetail = (FrameLayout) findViewById(R.id.frameLayoutOptionProductDetail);
        frameLayoutImagesProductDetail = (FrameLayout) findViewById(R.id.frameLayoutImagesProductDetail);
        btnBuyProductDetail = (Button) findViewById(R.id.btnBuyProductDetail);

        setSupportActionBar(toolbar);


        btnBuyProductDetail.setOnClickListener(this);
        ivImageProductDetail.setOnClickListener(this);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void loadData(){
        String productJson = null;
        if(getIntent().hasExtra("PRODUCT_JSON"))
            productJson = getIntent().getStringExtra("PRODUCT_JSON");
        else{
            Toast.makeText(ProductDetail.this, "Lỗi khi lấy thông tin sản phẩm, thử lại sau", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        product = new Gson().fromJson(productJson, Product.class);
        vLog.debug(getClass().getSimpleName(), "GET PRODUCT DATA OBJECT "+productJson);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(product.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Add data to buy package
        if(buyItem == null)
            buyItem = new BuyItem(product.getSku(),1);

        if(product.getStock_data()!=null && product.getStock_data().getQtyInteger()<=0){
            tvStatusProductDetail.setText("Đã hết hàng");
            Toast.makeText(ProductDetail.this, "Sản phẩm này đã hết hàng", Toast.LENGTH_SHORT).show();
            //btnBuyProductDetail.setOnClickListener(null);
        }else if(product.getStock_data()!=null&&product.getStock_data().getQtyInteger()<=10){
            tvStatusProductDetail.setTextColor(Color.BLUE);
            tvStatusProductDetail.setTextSize(15);
            tvStatusProductDetail.setText("Chỉ còn "+product.getStock_data().getQtyInteger()+" sản phẩm.");
        }

        List<String> list =  product.getAllFeatureVi();

        if(product.getSpecialPrice_integer()>0){
            tvPriceProductDetail.setText(String.valueOf(product.getSpecialPrice_integer())+"đ");
            tvOldPriceProductDetail.setText(String.valueOf(product.getPrice_integer() +"đ"));
            tvOldPriceProductDetail.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            tvPriceProductDetail.setText(String.valueOf(product.getPrice_integer())+"đ");
            tvOldPriceProductDetail.setText(null);
        }

        tvTitleProductDetail.setText(product.getName());
        tvDescriptionProductDetail.setText(parseFeature(product.getAllFeatureVi()));
        tvSkuProductDetail.setText(product.getSku());
        tvMadeinProductDetail.setText(product.getMade_in());
        tvBrandProductDetail.setText(product.getBrand_origin());
        Picasso.with(ProductDetail.this.getApplicationContext()).load(product.getFirstImage()).into(ivImageProductDetail);
        loadAllImages(product.getImages());
        loadOption(product.getOptions());

        //We need to get simalar products of this product
        getRelatedProduct(product.getName());
    }

    private void getRelatedProduct(String name) {
        Call<GetRelatedProductDataResponse> call = APIClient.getClient().create(ApiInterface.class).getRelatedProduct(name);
        call.enqueue(new Callback<GetRelatedProductDataResponse>() {
            @Override
            public void onResponse(Call<GetRelatedProductDataResponse> call, Response<GetRelatedProductDataResponse> response) {
                if(response.body()==null){
                    return;
                }
                List<Product> listRelated = MyUtils.filterProducts(response.body().getData());
                vLog.error(TAG, "Get "+listRelated.size() + " related products");
            }

            @Override
            public void onFailure(Call<GetRelatedProductDataResponse> call, Throwable t) {

            }
        });
    }

    private void loadOption(final List<OptionProduct> options) {
        vLog.debug(TAG,"OPTIONS count is " +options.size());
        if(options.size()>0){
            optionRequire = true;
        }
        else return;

        LinearLayout lLayout = new LinearLayout(this);
        TextView tvLabel = new TextView(this);
        final JsonObject option = new JsonObject();
        for(int i=0;i<options.size();i++){
            TextView tvOptionLabel = new TextView(this);
            tvOptionLabel.setText(options.get(i).getTitle());
            RadioGroup rG = new RadioGroup(this);

            final List<AdditionalOption> addtitions = options.get(i).getAdditional_fields();
            for (int j=0;j<addtitions.size();j++){
                RadioButton rB = new RadioButton(this);
                rB.setText(addtitions.get(j).getTitle());
                rB.setId(i*10+j); //make sure these radio ids is unique in this layout
                final int finalJ = j;
                final int finalI = i;
                rB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        option.addProperty(options.get(finalI).getOption_id(), addtitions.get(finalJ).getValue_id());
                        buyItem.setOptions(option);
                    }
                });
                rG.addView(rB);
            }
            lLayout.addView(tvOptionLabel);
            lLayout.addView(rG);
        }
        frameLayoutOptionProductDetail.addView(lLayout);
    }

    private void loadAllImages(final List<Image> images) {
        vLog.debug(TAG,"IMAGES count is " +images.size());
        HorizontalScrollView hScrollView = new HorizontalScrollView(this);
        LinearLayout lLayout = new LinearLayout(this);


        for(int i =0; i< images.size();i++){
            final Image img = images.get(i);
            ImageView ivImage = new ImageView(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.height=400;

            ivImage.setPadding(10,10,10,10);

            ivImage.setLayoutParams(param);
            Picasso.with(ProductDetail.this).load(img.getFullURL()).into(ivImage);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(ProductDetail.this, img.getEtag(),Toast.LENGTH_SHORT).show();
                }
            });
            lLayout.addView(ivImage);
        }

        hScrollView.addView(lLayout);
        frameLayoutImagesProductDetail.addView(hScrollView);
    }

    private String parseFeature(List<String> s){
        StringBuilder strB = new StringBuilder();
        for(int i=0; i<s.size();i++){
            strB.append("\t"+s.get(i)+"\n");
        }
        return strB.toString();
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

    @Override
    public void onClick(View v) {
        int clicked = v.getId();
        vLog.debug(TAG, "Clicked "+clicked);

        switch (clicked){
            case R.id.btnBuyProductDetail:
                if(optionRequire){
                    if(buyItem.getOptions().size()!=product.getOptions().size()){
                        Toast.makeText(this, "Vui lòng chọn màu sắc, kích thước,...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                vLog.debug(TAG, "Payload add to cart " + new Gson().toJson(buyItem));

                ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
                JsonObject payload = new JsonObject();
                Call<AddCartDataResponse> call = null;
                payload.addProperty("sku", buyItem.getSku());
                payload.addProperty("qty", buyItem.getQty());
                payload.add("options", buyItem.getOptions());

                if(payload!=null)
                     call = apiService.addCart(payload);

                call.enqueue(new Callback<AddCartDataResponse>() {
                    @Override
                    public void onResponse(Call<AddCartDataResponse> call, Response<AddCartDataResponse> response) {
                        AddCartDataResponse addCartDataResponse = response.body();
                        if(!addCartDataResponse.isOk()){
                            Toast.makeText(ProductDetail.this, "Lỗi khi thêm vào giỏ hàng, xin thử lại", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Set cart to global
                        Toast.makeText(ProductDetail.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        CartResponse cartResponse = addCartDataResponse.getCart();
                        MyGlobal.setMyCart(cartResponse);

                        vLog.debug(TAG, "LastCartId "+String.valueOf(response.body().getLastCartId()));

                        if(cartResponse==null){
                            return;
                        }
                        Intent cartView = new Intent(ProductDetail.this, CartView.class);
                        startActivity(cartView);
                    }

                    @Override
                    public void onFailure(Call<AddCartDataResponse> call, Throwable t) {
                        vLog.error(TAG, String.valueOf(t));
                    }
                });

                break;

        }
    }
}
