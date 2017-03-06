package taembe.example.blackwine.taembe.view.thankyou;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.MainActivity;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;

public class ThankYouView extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    TextView tvOrderNumberThankyou;
    Button btnReturnShopThankyou;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_view);
        init();
    }

    private void init() {
        tvOrderNumberThankyou = (TextView) findViewById(R.id.tvOrderNumberThankyou);
        btnReturnShopThankyou = (Button) findViewById(R.id.btnReturnShopThankyou);
        pb = (ProgressBar) findViewById(R.id.progressBarOrderNumberCheckout);
        String lastCartId = null;
        pb.setVisibility(View.VISIBLE);
        btnReturnShopThankyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(ThankYouView.this, MainActivity.class);
                startActivity(home);
            }
        });

        if(getIntent().hasExtra("lastCartId")){
            lastCartId = getIntent().getStringExtra("lastCartId").toString();
        }

        final Timer schedule = new Timer();
        final String finalLastCartId = lastCartId;
        TimerTask task = new TimerTask() {
            int limitTimes = 10;
            @Override
            public void run() {
                limitTimes--;
                vLog.debug(TAG, limitTimes+ " attempts remain to get order number");
                if(limitTimes==0){
                    schedule.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.INVISIBLE);
                            tvOrderNumberThankyou.setTextSize(15);
                            tvOrderNumberThankyou.setTextColor(Color.parseColor("#ecbe07"));
                            tvOrderNumberThankyou.setText("Lỗi khi lấy mã đơn hàng\nChúng tôi sẽ thông báo cho bạn sau khi xử lí hoàn tất");
                        }
                    });

                }
                getOrderNumber(schedule, finalLastCartId);
            }
        };

        schedule.schedule(task, 1000, 2000);
    }

    private void getOrderNumber(final Timer schedule, String finalLastCartId){
        Call<JsonObject> call = APIClient.getClient().create(ApiInterface.class).getNumberOrder(finalLastCartId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body()==null)
                    return;
                else{
                    vLog.debug(TAG, "Get order number is "+ new Gson().toJson(response.body()));
                    if(response.body().has("order_number")){
                        String orderNumber = null;
                        orderNumber = response.body().get("order_number").getAsString();
                        pb.setVisibility(View.INVISIBLE);
                        tvOrderNumberThankyou.setText(orderNumber);
                        schedule.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
}
