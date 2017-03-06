package taembe.example.blackwine.taembe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import taembe.example.blackwine.taembe.adapter.ViewPagerAdapter;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.view.cart.CartView;
import taembe.example.blackwine.taembe.view.fragments.ProductHomeFragment;
import taembe.example.blackwine.taembe.view.fragments.PromotionFragment;
import taembe.example.blackwine.taembe.view.search.SearchBarView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout mainView, edgeMenuView;
    private DrawerLayout drawLayout;
    private ActionBarDrawerToggle actionDrawToggle;
    private FrameLayout fragmentEdgeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "PHIÊN BẢN THỬ NGHIỆM\n Chưa lưu session người dùng\n Chưa có category\n Chưa có tính năng Login", Toast.LENGTH_LONG).show();
        init();
        setupPagerView();
    }

    private void init(){

        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle(R.string.app_name);
        setupEdgeMenu();
        //actionBar.setIcon(R.drawable.logo_2x);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mainView = (RelativeLayout) findViewById(R.id.activity_main);
        edgeMenuView = (RelativeLayout) findViewById(R.id.edgeMenu);
        //mainView.setTranslationX(1);
        actionDrawToggle = new ActionBarDrawerToggle(this, drawLayout,this.toolbar,R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                vLog.debug(TAG,String.valueOf(slideOffset));
                //mainView.setTranslationX(slideOffset * edgeMenuView.getWidth());
                drawLayout.bringChildToFront(mainView);
                drawLayout.requestLayout();
            }
        };

        //fragmentEdgeMenu = (FrameLayout) findViewById(R.id.framelayout_edge_left_menu_container);


    }

    private void setupEdgeMenu() {
//        fragmentEdgeMenu.bringToFront();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fmt = fm.beginTransaction();
//        EdgeLeftMenuFragment eMenu = new EdgeLeftMenuFragment();
//        fmt.replace(fragmentEdgeMenu.getId(),eMenu);
//        fmt.commit();
    }

    private void setupPagerView(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PromotionFragment(), getString(R.string.Promotion));
        viewPagerAdapter.addFragment(new ProductHomeFragment(), getString(R.string.Product));
//        viewPagerAdapter.addFragment(new CategoryFragment(), "Danh mục");
//        viewPagerAdapter.addFragment(new OptionFragment(), "Mục khác");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent searchBarView = new Intent(MainActivity.this, SearchBarView.class);
            startActivity(searchBarView);
            return true;
        }

        if (id == R.id.action_cartview) {
            Intent cartView = new Intent(MainActivity.this, CartView.class);
            startActivity(cartView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
