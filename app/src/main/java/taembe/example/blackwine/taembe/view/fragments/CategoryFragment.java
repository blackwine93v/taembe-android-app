package taembe.example.blackwine.taembe.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.adapter.RecyclerListCategoryAdapter;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.model.stores.category.Category;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.model.stores.search.SearchDataResponse;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;

/**
 * Created by Blackwine on 2/20/2017.
 */

public class CategoryFragment extends Fragment {
    private  final String TAG = getClass().getSimpleName();
    RecyclerView recyclerViewCategoryContainer;
    //LinearLayout iLayoutDefaultListCategoryContainer;
    private List<Category> lists;
    private Category category;

    public CategoryFragment(Category category) {
        this.category = category;
    }

    public CategoryFragment(String tilte, List<Product> lists) {
    }

    public CategoryFragment(List<Category> lists) {
        this.lists = lists;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        init(view);
        return view;
    }

    private  void init(View view){
       // iLayoutDefaultListCategoryContainer = (LinearLayout) view.findViewById(R.id.iLayoutDefaultListCategoryContainer);
        recyclerViewCategoryContainer = (RecyclerView) view.findViewById(R.id.recyclerViewCategoryContainer);

        if(lists!=null) {
            RecyclerListCategoryAdapter adapter = new RecyclerListCategoryAdapter(view.getContext(), lists);
            LinearLayoutManager layout = new LinearLayoutManager(CategoryFragment.this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewCategoryContainer.setLayoutManager(layout);
            recyclerViewCategoryContainer.setAdapter(adapter);

            recyclerViewCategoryContainer.scrollToPosition(0);
            adapter.setOnClickItemEvent(new RecyclerListCategoryAdapter.setOnClickItem() {
                @Override
                public void onClickViewAll(Category item) {
                    vLog.debug(TAG,"View all in category "+item.getName()+", url_key is "+item.getUrl_key());
                    loadCategory(item);
                }
            });
        }else if(category!=null){
            loadCategory(category);
        }else{
            return;
        }
    }

    private void loadCategory(final Category category) {
        Call<SearchDataResponse> call = APIClient.getClient().create(ApiInterface.class).getProductFromCategory(category.getUrl_key());
        call.enqueue(new Callback<SearchDataResponse>() {
            @Override
            public void onResponse(Call<SearchDataResponse> call, Response<SearchDataResponse> response) {
                if(response.errorBody()!=null){
                    vLog.error(TAG,"Error when getProductFromCategory "+category.getUrl_key());
                    return;
                }else if(response.body()!=null){
                    if(response.body().getChildrenCategories().size()>0)
                        MyUtils.switchToFragment(CategoryFragment.this, R.id.frameLayoutCategoryContainer, new CategoryFragment(response.body().getChildrenCategories()));
                    else{
                        Intent searchView = new Intent(CategoryFragment.this.getContext(), taembe.example.blackwine.taembe.view.search.SearchView.class);
                        searchView.putExtra("category_url",category.getUrl_key());
                        searchView.putExtra("title", category.getName());
                        startActivity(searchView);
                        vLog.debug(TAG, "Search for category: "+ category.getUrl_key());
                    }

                }
            }

            @Override
            public void onFailure(Call<SearchDataResponse> call, Throwable t) {

            }
        });
    }
}
