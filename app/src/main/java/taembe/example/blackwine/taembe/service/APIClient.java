package taembe.example.blackwine.taembe.service;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.vLog;


/**
 * Created by Blackwine on 2/21/2017.
 */

public class APIClient {
    private static Retrofit retrofit = null;
    public static final String URL_CLIENT = MyGlobal.getBaseUrl();
    public static final String TAG = "APIInterface";
    public static Retrofit getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                //Set cookie to header
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Cookie", MyGlobal.getCookie())
                        .method(original.method(), original.body())
                        .build();

                vLog.debug(TAG, "SEND REQUEST TO "+original.url().url().toString());
                //Get cookie from response and save to Global cookie
                Response response =  chain.proceed(request);
                if(response!=null && response.header("set-cookie")!=null && !response.header("set-cookie").isEmpty()){
                    HashSet<String> cookies = new HashSet<>();

                    for (String header : response.headers("set-cookie")) {
                        cookies.add(header);
                    }
                    MyGlobal.setCookie(response.header("set-cookie").toString());
                }
                return response;
            }
        });




        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_CLIENT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        return retrofit;
    }
}
