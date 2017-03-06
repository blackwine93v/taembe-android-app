package taembe.example.blackwine.taembe.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.adapter.ViewPagerAdapter;

public class MainFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    TabLayout tabLayout = null;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupPagerView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View mView;
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        init(mView);
        return mView;
    }
    private void init(View view){
        toolbar = (Toolbar) view.findViewById(R.id.action_toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
    }

    private void setupPagerView(MainFragment view){
        viewPagerAdapter = new ViewPagerAdapter(view.getChildFragmentManager());
        viewPagerAdapter.addFragment(new PromotionFragment(), getString(R.string.Promotion));
        viewPagerAdapter.addFragment(new ProductHomeFragment(), getString(R.string.Product));
//        viewPagerAdapter.addFragment(new CategoryFragment(), "Danh mục");
//        viewPagerAdapter.addFragment(new OptionFragment(), "Mục khác");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
