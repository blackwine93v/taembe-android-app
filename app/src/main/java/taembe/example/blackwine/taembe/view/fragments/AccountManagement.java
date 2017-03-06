package taembe.example.blackwine.taembe.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;

/**
 * Created by Blackwine on 3/3/2017.
 */

public class AccountManagement extends Fragment implements View.OnClickListener{
    Button btnLogout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_management, container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogout:
                logout();
                break;

        }
    }

    private void logout() {
        Call<JsonObject> call = APIClient.getClient().create(ApiInterface.class).logout();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body()!=null && response.body().has("status")){
                    Snackbar.make(getView(), "Đã thoát tài khoản của bạn", Snackbar.LENGTH_LONG).show();
                    MyGlobal.setUser(null);
                    MyUtils.switchToFragment(AccountManagement.this, R.id.frameLayoutNavContainer, new LoginFragment());
                }
                else
                    Snackbar.make(getView(), "Có lỗi, xin thử lại", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void switchToLogin() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fmt = null;
        fmt = fm.beginTransaction().replace(R.id.frameLayoutNavContainer, new LoginFragment());
        fmt.commit();
    }
}
