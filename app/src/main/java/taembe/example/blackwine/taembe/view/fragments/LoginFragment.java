package taembe.example.blackwine.taembe.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.model.stores.user.User;
import taembe.example.blackwine.taembe.service.APIClient;
import taembe.example.blackwine.taembe.service.ApiInterface;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Blackwine on 3/2/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{
    EditText editTextUsername, editTextPassword;
    Button btnSubmitLogin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =null;
        view = inflater.inflate(R.layout.fragment_login,container, false);

        init(view);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init(View view) {
        editTextUsername = (EditText) view.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        btnSubmitLogin = (Button) view.findViewById(R.id.btnSubmitLogin);

        btnSubmitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSubmitLogin:
                submitLogin();
                break;
        }
    }

    private void submitLogin() {
        //hide keyboard
        InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        //need to validate data before sending
        if(!validate()){
            return;
        }

        JsonObject payload = new JsonObject();
        payload.addProperty("email", editTextUsername.getText().toString());
        payload.addProperty("password", editTextPassword.getText().toString());
        Call<User> call = APIClient.getClient().create(ApiInterface.class).login(payload);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.errorBody()!=null){
                    Snackbar.make(LoginFragment.this.getView(), "Lỗi khi đăng nhập, xin thử lại", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    if(response.body()!=null && response.body().getName()!=null){
                        Snackbar.make(LoginFragment.this.getView(), "Đăng nhập thành công", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        MyGlobal.setUser(response.body());
                        MyUtils.switchToFragment(LoginFragment.this, R.id.frameLayoutNavContainer, new AccountManagement());
                    }else{
                        Snackbar.make(LoginFragment.this.getView(), "Sai thông tin, xin thử lại", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void switchToAccountManage() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fmt = null;
        fmt = fm.beginTransaction().replace(R.id.frameLayoutNavContainer, new AccountManagement());
        fmt.commit();
    }

    private boolean validate() {
        if(editTextUsername.getText().length()==0){
            Snackbar.make(this.getView(), "Tên tài khoản không được để trống", Snackbar.LENGTH_LONG).show();
            editTextUsername.setBackgroundColor(getResources().getColor(R.color.Indigo100));
            editTextUsername.setHintTextColor(Color.RED);

            return false;
        }
        if(editTextPassword.getText().length()==0){
            Snackbar.make(this.getView(), "Mật khẩu không được để trống", Snackbar.LENGTH_LONG).show();
            editTextPassword.setBackgroundColor(getResources().getColor(R.color.Indigo100));
            editTextPassword.setHintTextColor(Color.RED);
            return false;
        }

        return true;
    }
}
