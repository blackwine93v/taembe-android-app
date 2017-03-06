package taembe.example.blackwine.taembe.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.adapter.RecyclerListProductAdapter;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.home.HomeData;
import taembe.example.blackwine.taembe.model.stores.cart.CartResponse;
import taembe.example.blackwine.taembe.model.stores.category.Category;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.product.ProductDetail;

/**
 * Created by Blackwine on 2/20/2017.
 */

public class ProductHomeFragment extends Fragment {
    private View view;
    private final String TAG = getClass().getSimpleName();
    private FrameLayout frameLayoutTitleCategory, frameLayoutCategoryContainer;


    public ProductHomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_product_home, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        frameLayoutTitleCategory = (FrameLayout) view.findViewById(R.id.frameLayoutTitleCategory);
        frameLayoutCategoryContainer = (FrameLayout) view.findViewById(R.id.frameLayoutCategoryContainer);
        getHomeData(view);
    }

    private void getHomeData(final View view){
        vLog.debug(TAG,"getHomeData");
        final LinearLayout lLayout = new LinearLayout(view.getContext());
        lLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lLayout.setOrientation(LinearLayout.VERTICAL);

        ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
        Call<HomeData> call = apiService.getHomeData();
        call.enqueue(new Callback<HomeData>() {
            @Override
            public void onResponse(Call<HomeData> call, Response<HomeData> response) {
                if(response.errorBody()!=null){
                    Toast.makeText(ProductHomeFragment.this.getContext(), "Lỗi khi tải dữ liệu, hãy thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }else if(response.body()!=null){
                    CartResponse cart = response.body().getCart();
                    MyGlobal.setMyCart(cart);
                    if(response.body().getHotCategories().size()>0)
                        renderListCategory(response.body().getHotCategories());

                    List<Product> hotProducts = response.body().getHotProducts();
                    List<Product> newProducts = response.body().getNewProducts();

                    //filter products is disabled
                    hotProducts = MyUtils.filterProducts(hotProducts);
                    newProducts = MyUtils.filterProducts(newProducts);

                    new ListProduct(view, lLayout, hotProducts, getString(R.string.hot_product)).create();
                    new ListProduct(view, lLayout, newProducts, getString(R.string.new_product)).create();

                    frameLayoutCategoryContainer.addView(lLayout);

                    vLog.debug(TAG, "Get HotProducts " +String.valueOf(response.body().getHotProducts().size()));
                    vLog.debug(TAG, "Get NewProducts " +String.valueOf(response.body().getNewProducts().size()));
                }
            }

            @Override
            public void onFailure(Call<HomeData> call, Throwable t) {
                vLog.debug(TAG, String.valueOf(t));
            }
        });
    }

    private void renderListCategory(List<Category> hotCategories) {
        LinearLayout lLayout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lLayout.setOrientation(LinearLayout.HORIZONTAL);
        lLayout.setLayoutParams(params);

        for(int i=0;i<hotCategories.size();i++){
            final Category category = hotCategories.get(i);
            if(category.getName()!=null && category.getUrl_key()!=null){
                TextView tvTitle = new TextView(this.getContext());
                tvTitle.setText(category.getName());
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(20, 20, 20, 20);
                tvTitle.setPadding(20, 20, 20, 20);
                tvTitle.setTextSize(20);
                tvTitle.setTextColor(Color.WHITE);
                tvTitle.setBackgroundResource(R.drawable.round_bg);
                tvTitle.setLayoutParams(textParams);

                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.switchToFragment(ProductHomeFragment.this, R.id.frameLayoutCategoryContainer, new CategoryFragment(category));
                    }
                });

                lLayout.addView(tvTitle);
            }
        }
        frameLayoutTitleCategory.addView(lLayout);
    }

    public class ListProduct{
        private View view;
        private List<Product> products;
        private RecyclerView recyclerView = null;
        private LinearLayout lLayout;
        private String name;

        public ListProduct(View view, LinearLayout lLayout, List<Product> products, String name) {
            this.products = products;
            this.view = view;
            this.lLayout = lLayout;
            this.name = name;
        }

        //create new recycler view
        private RecyclerView recyclerViewMaker(String title) {
            LinearLayout lLayoutElement = new LinearLayout(view.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);
            lLayoutElement.setOrientation(LinearLayout.VERTICAL);

            TextView tvTitle = new TextView(view.getContext());
            tvTitle.setText(title);

            RecyclerView myRecyclerView = new RecyclerView(view.getContext());

            lLayoutElement.addView(tvTitle);
            lLayoutElement.addView(myRecyclerView);
            lLayout.addView(lLayoutElement);
            return myRecyclerView;
        }


        void create(){
            recyclerView = recyclerViewMaker(name);
            RecyclerListProductAdapter adapter = new RecyclerListProductAdapter(ProductHomeFragment.this.getContext(), products, RecyclerListProductAdapter.LAYOUT_PRODUCT_TYPE_HORIZONTAL_TYPE);
            if(adapter == null)
                return;
            recyclerView.setAdapter(adapter);

            LinearLayoutManager layout = new LinearLayoutManager(ProductHomeFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layout);


            adapter.setOnClickItemEvent(new RecyclerListProductAdapter.setOnClickItem() {
                @Override
                public void onClickItemProduct(Product product) {
                    vLog.debug(TAG, "Clicked on product " + product.getName());
                    Intent viewProductDetail = new Intent(ProductHomeFragment.this.getContext(), ProductDetail.class);
                    String productJson = new Gson().toJson(product);
                    viewProductDetail.putExtra("PRODUCT_JSON", productJson);
                    startActivity(viewProductDetail);
                }

                @Override
                public void onClickButtonBuy(Product product) {

                }
            });

        }
    }
    

    @Override
    public void onResume() {
        super.onResume();
    }
}
