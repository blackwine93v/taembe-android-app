package taembe.example.blackwine.taembe.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.adapter.RecyclerListProductAdapter;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.model.stores.search.SearchDataResponse;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.product.ProductDetail;

public class SearchView extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabScrollTopSearch;
    private int currentPage = 0;
    private final int NUMBER_PER_PAGE = 12;
    private List<Product> productData = new ArrayList<>();
    private RecyclerListProductAdapter adapter;
    private Map<String, String> queryMap = new HashMap<>();
    private final int QUERY_SEARCH=1, CATEGORY_PATH_SEARCH=2;
    private int type;
    private String category_url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearchView);
        fabScrollTopSearch = (FloatingActionButton) findViewById(R.id.fabScrollTopSearch);

        fabScrollTopSearch.setOnClickListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tìm kiếm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("fullUrl")){
            String fullUrl = getIntent().getStringExtra("fullUrl");
            vLog.debug(TAG, "Search URL: "+ fullUrl);
            type = QUERY_SEARCH;
            queryMap = parseQuery(fullUrl);
            loadSearchData();
        }

        if(getIntent().hasExtra("title")){
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }

        if(getIntent().hasExtra("q")){
            queryMap.put("q",getIntent().getStringExtra("q"));
            type = QUERY_SEARCH;
            loadSearchData();
        }

        if(getIntent().hasExtra("category_url")){
            category_url = getIntent().getStringExtra("category_url");
            type = CATEGORY_PATH_SEARCH;
            loadSearchData();
        }
    }



    private void loadSearchData() {
        //Update startAt point
        queryMap.put("json", "1");
        queryMap.put("start", String.valueOf(currentPage*NUMBER_PER_PAGE));
        currentPage++;

        Call<SearchDataResponse> call = null;

        if(type==QUERY_SEARCH)
            call = APIClient.getClient().create(ApiInterface.class).searchProduct(queryMap);

        if(type == CATEGORY_PATH_SEARCH)
            call = APIClient.getClient().create(ApiInterface.class).getProductFromCategory(category_url, queryMap);

        call.enqueue(new Callback<SearchDataResponse>() {
            @Override
            public void onResponse(Call<SearchDataResponse> call, Response<SearchDataResponse> response) {
                if(response.errorBody()!=null){
                    Toast.makeText(SearchView.this,"Lỗi khi lấy dữ liệu, xin thử lại", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    return;
                }

                else if(response.body()!=null && response.body().getProducts().size()>0){
                    reloadData(response.body().getProducts());
                }
                else{
                    Toast.makeText(SearchView.this,"Đang hiển thị "+productData.size()+" trên "+response.body().getTotalResults()+" kết quả tìm thấy", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<SearchDataResponse> call, Throwable t) {

            }
        });
    }

    private void reloadData(List<Product> products) {
        this.productData.addAll(products);
        if(adapter==null){
            adapter = new RecyclerListProductAdapter(SearchView.this, productData, RecyclerListProductAdapter.LAYOUT_PRODUCT_TYPE_VERTICAL_TYPE);
            final RecyclerView.LayoutManager layoutManage = new LinearLayoutManager(SearchView.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManage);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent()
                            == recyclerView.computeVerticalScrollRange()){
                        Toast.makeText(SearchView.this, "Đang tải thêm sản phẩm...", Toast.LENGTH_SHORT).show();
                        loadSearchData();
                    }

                    if(recyclerView.computeVerticalScrollOffset()
                            > recyclerView.computeVerticalScrollExtent()+3000){
                        fabScrollTopSearch.setVisibility(View.VISIBLE);
                    }
                    else
                        fabScrollTopSearch.setVisibility(View.INVISIBLE);
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            adapter.setOnClickItemEvent(new RecyclerListProductAdapter.setOnClickItem() {
                @Override
                public void onClickItemProduct(Product product) {
                    vLog.debug(TAG, "Clicked on product " + product.getName());
                    Intent viewProductDetail = new Intent(SearchView.this, ProductDetail.class);
                    String productJson = new Gson().toJson(product);
                    viewProductDetail.putExtra("PRODUCT_JSON", productJson);
                    startActivity(viewProductDetail);
                }

                @Override
                public void onClickButtonBuy(Product product) {

                }
            });
        }

        adapter.notifyDataSetChanged();
    }

    private Map<String, String> parseQuery(String fullUrl) {
        Map<String, String> data = new HashMap<>();
        data.put("json","1");

        if(fullUrl.indexOf("?")>=0){
            String query = fullUrl.substring(fullUrl.indexOf("?")+1);
            String[] part = query.split("&");
            for(int i=0;i<part.length;i++){
                String[] element = part[i].split("=");
                data.put(element[0], element[1]);
            }
        }
        return data;
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
        switch (v.getId()){
            case R.id.fabScrollTopSearch:
                recyclerView.scrollToPosition(10);
                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }
}
