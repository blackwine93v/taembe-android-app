package taembe.example.blackwine.taembe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Blackwine on 2/16/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitle = new ArrayList<>();

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }

    public void addFragment(Fragment fragment, String tilte){
        this.fragments.add(fragment);
        this.tabTitle.add(tilte);
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
