package taembe.example.blackwine.taembe.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.model.stores.product.Product;
import taembe.example.blackwine.taembe.model.stores.user.User;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;

/**
 * Created by Blackwine on 2/25/2017.
 */

public class MyUtils {
    static public List<Product> filterProducts(List<Product> products) {
        for(int i=0;i<products.size();i++){
            Product product = products.get(i);
            if(product.getStatus().compareTo("2")==0){
                products.remove(i);
            }
        }
        return products;
    }

    static public void switchToFragment(Fragment activity, int container, Fragment fragment) {
        if(activity == null || fragment == null)
            return;
        FragmentManager fm = activity.getActivity().getSupportFragmentManager();
        FragmentTransaction fmt = null;
        fmt = fm.beginTransaction();
        //fmt.replace(container, fragment);
        fmt.add(container, fragment, fragment.getClass().getSimpleName());
        fmt.addToBackStack(fragment.getClass().getSimpleName());
        //replace(container, fragment);
        fmt.commit();
    }

    static public void getStatusLogin(){
        Call<User> call = APIClient.getClient().create(ApiInterface.class).getStatus();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body()!=null && response.body().isLogin()){
                    MyGlobal.setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
