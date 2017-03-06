package taembe.example.blackwine.taembe.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.model.stores.promotion.Promotion;
import taembe.example.blackwine.taembe.model.stores.promotion.PromotionDataResponse;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;
import taembe.example.blackwine.taembe.view.search.SearchView;

/**
 * Created by Blackwine on 2/20/2017.
 */

public class PromotionFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    FrameLayout frameLayoutPromotion;
    TextView tvTitlePromotion;

    public PromotionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        init(view);
        return view;
    }

    private void loadPromotion(final View view) {
        Call<PromotionDataResponse> call = APIClient.getClient().create(ApiInterface.class).getAllPromotion();
        call.enqueue(new Callback<PromotionDataResponse>() {
            @Override
            public void onResponse(Call<PromotionDataResponse> call, final Response<PromotionDataResponse> response) {
                if(response.body()!=null && response.body().getAllPromo().size()>0){
                    renderPromotion(response.body().getAllPromo());
                }
                if(response.errorBody()!=null){
                    Toast.makeText(PromotionFragment.this.getContext(), "Lỗi khi lấy thông tin khuyến mãi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PromotionDataResponse> call, Throwable t) {

            }
        });
    }

    private void renderPromotion(List<Promotion> datas) {
        if(this.getContext()==null)
            return;
        LinearLayout lLayout = new LinearLayout(this.getContext());
        ScrollView scrollView = new ScrollView(this.getContext());
        lLayout.setOrientation(LinearLayout.VERTICAL);


        for(int i =0;i<datas.size();i++){
            final Promotion data = datas.get(i);
            ImageView ivImgPromo = new ImageView(this.getContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout lLayoutPromo = new LinearLayout(this.getContext());
            TextView title = new TextView(this.getContext());
            title.setPadding(5,5,5,5);
            title.setTextColor(Color.BLACK);
            title.setText(data.getTitle());

            String url = data.getFullUrlImg();
            if(data.getType().compareTo("Promotion")==0) {
                lLayoutPromo.setOrientation(LinearLayout.HORIZONTAL);
                param.height=400;
                param.width=400;
                ivImgPromo.setLayoutParams(param);
                Picasso.with(this.getContext()).load(url).into(ivImgPromo);
                lLayoutPromo.addView(ivImgPromo);
                lLayoutPromo.addView(title);
            }
            else if(data.getType().compareTo("Banner")==0) {
                lLayoutPromo.setOrientation(LinearLayout.VERTICAL);
                lLayoutPromo.addView(title);
                Picasso.with(this.getContext()).load(url).resize(1200, 400).into(ivImgPromo);
                lLayoutPromo.addView(ivImgPromo);
            }
            LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramLayout.bottomMargin = 100;
            lLayoutPromo.setLayoutParams(paramLayout);
            lLayoutPromo.setPadding(10,10,10,10);
            lLayoutPromo.setBackgroundColor(Color.WHITE);
            lLayoutPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchView = new Intent(PromotionFragment.this.getContext(), SearchView.class);
                    searchView.putExtra("fullUrl", data.getLink());
                    searchView.putExtra("title", data.getTitle());
                    startActivity(searchView);
                }
            });
            lLayout.addView(lLayoutPromo);
        }
        scrollView.addView(lLayout);
        frameLayoutPromotion.addView(scrollView);
    }

    private  void init(View view){
        frameLayoutPromotion = (FrameLayout) view.findViewById(R.id.frameLayoutPromotion);
        loadPromotion(view);
    }
}
