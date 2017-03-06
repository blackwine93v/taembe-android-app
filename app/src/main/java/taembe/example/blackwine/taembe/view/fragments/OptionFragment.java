package taembe.example.blackwine.taembe.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taembe.example.blackwine.taembe.R;

/**
 * Created by Blackwine on 2/20/2017.
 */

public class OptionFragment extends Fragment{
    public OptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        init(view);
        return view;
    }

    private  void init(View view){

    }
}
