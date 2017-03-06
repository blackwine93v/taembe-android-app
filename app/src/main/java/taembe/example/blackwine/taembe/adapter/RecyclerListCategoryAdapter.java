package taembe.example.blackwine.taembe.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.category.Category;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.model.stores.search.SearchDataResponse;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.product.ProductDetail;

/**
 * Created by Blackwine on 2/13/2017.
 */

public class RecyclerListCategoryAdapter extends RecyclerView.Adapter<RecyclerListCategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> items;
    private final String TAG = getClass().getSimpleName();
    private int LAYOUT_CATEGORY_ITEM_TYPE = R.layout.item_category_vertical;


    public RecyclerListCategoryAdapter(Context context, List<Category> items) {
//        if(items==null || context == null)
//            return;
        this.context = context;
        this.items = items;
    }


    public interface setOnClickItem {
        void onClickViewAll(Category item);
    }

    private  RecyclerListCategoryAdapter.setOnClickItem listener;
    public  void setOnClickItemEvent(RecyclerListCategoryAdapter.setOnClickItem listener){
        this.listener = listener;
    }

    @Override
    public RecyclerListCategoryAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View itemView =  inflater.inflate(LAYOUT_CATEGORY_ITEM_TYPE ,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Category item = items.get(position);
        TextView tvViewAllCategory = holder.tvViewAllCategory;
        TextView tvTitleCategory = holder.tvTitleCategory;
        RecyclerView recyclerViewCategory = holder.recyclerViewCategory;

        tvTitleCategory.setText(item.getName());
        tvViewAllCategory.setText("Xem tất cả");
        renderCategory(recyclerViewCategory, item);

        tvViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickViewAll(item);
                }
            }
        });
    }

    private void renderCategory(RecyclerView recyclerViewCategory, Category item) {
        getProductFromCategory(item, recyclerViewCategory);
    }

    private void getProductFromCategory(final Category item, final RecyclerView recyclerViewCategory) {
        if(item.getUrl_key()==null){
            vLog.debug(TAG, item.getName() + " have no url_key");
            return;
        }

        Call<SearchDataResponse> call = APIClient.getClient().create(ApiInterface.class).getProductFromCategory(item.getUrl_key());
        call.enqueue(new Callback<SearchDataResponse>() {
            @Override
            public void onResponse(Call<SearchDataResponse> call, Response<SearchDataResponse> response) {
                if(response.errorBody()!=null){
                    vLog.error(TAG, item.getName() + response.raw().message());
                    return;
                }else if(response.body()!=null){
                    vLog.debug(TAG, item.getName() + " response successful");
                    renderListProduct(response.body().getProducts(), recyclerViewCategory);
                }
            }

            @Override
            public void onFailure(Call<SearchDataResponse> call, Throwable t) {
                vLog.error(TAG, t.getMessage());
            }
        });
    }

    private void renderListProduct(List<Product> products, RecyclerView recyclerViewCategory) {
        if(products==null || recyclerViewCategory == null)
            return;
        RecyclerListProductAdapter adapter = new RecyclerListProductAdapter(context,products , RecyclerListProductAdapter.LAYOUT_PRODUCT_TYPE_HORIZONTAL_TYPE);
        if(adapter == null)
            return;

        recyclerViewCategory.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layout);

        adapter.setOnClickItemEvent(new RecyclerListProductAdapter.setOnClickItem() {
            @Override
            public void onClickItemProduct(Product product) {
                vLog.debug(TAG, "Clicked on product " + product.getName());
                Intent viewProductDetail = new Intent(context, ProductDetail.class);
                String productJson = new Gson().toJson(product);
                viewProductDetail.putExtra("PRODUCT_JSON", productJson);
                context.startActivity(viewProductDetail);
            }

            @Override
            public void onClickButtonBuy(Product product) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvViewAllCategory, tvTitleCategory;
        public RecyclerView recyclerViewCategory;

        private final String TAG = getClass().getSimpleName();

        public ViewHolder(View itemView) {
            super(itemView);
            tvViewAllCategory = (TextView) itemView.findViewById(R.id.tvViewAllCategory);
            tvTitleCategory = (TextView) itemView.findViewById(R.id.tvTitleCategory);
            recyclerViewCategory = (RecyclerView) itemView.findViewById(R.id.recyclerViewCategory);
        }
    }
}
