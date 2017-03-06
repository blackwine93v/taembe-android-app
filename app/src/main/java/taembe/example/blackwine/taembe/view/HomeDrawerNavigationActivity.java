package taembe.example.blackwine.taembe.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.MyGlobal;
import taembe.example.blackwine.taembe.common.MyUtils;
import taembe.example.blackwine.taembe.common.vLog;
import taembe.example.blackwine.taembe.view.cart.CartView;
import taembe.example.blackwine.taembe.view.fragments.AccountManagement;
import taembe.example.blackwine.taembe.view.fragments.LoginFragment;
import taembe.example.blackwine.taembe.view.fragments.MainFragment;
import taembe.example.blackwine.taembe.view.search.SearchView;

public class HomeDrawerNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private final String TAG = getClass().getSimpleName();

    private boolean doubleClickBackToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUS();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Default view
        switchFragment(new MainFragment());

        //get login status
        MyUtils.getStatusLogin();
        //checkUpdate();
    }

    /*private void checkUpdate() {
        Call<JsonObject> call = APIClient.getClient().create(ApiInterface.class).getLastestVersion();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.errorBody()!=null){
                    vLog.error(TAG,"Error when check update");
                }else{
                    //get destination to update file and set Uri

                    ActivityCompat.requestPermissions(HomeDrawerNavigationActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                    String fileName = "taembe.apk";
                    destination += fileName;
                    final Uri uri = Uri.parse("file://" + destination);

                    //Delete update file if exists
                    File file = new File(destination);
                    if (file.exists())
                        //file.delete() - test this, I think sometimes it doesnt work
                        file.delete();

                    //get url of app on server
                    String url = (MyGlobal.BASE_URL + response.body().get("link").toString()).replace("\"","");
                    vLog.debug(TAG, url);
                    //set downloadmanager
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("Cập nhật Tã em bé");
                    request.setTitle(HomeDrawerNavigationActivity.this.getString(R.string.app_name));

                    //set destination
                    request.setDestinationUri(uri);

                    // get download service and enqueue file
                    final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    final long downloadId = manager.enqueue(request);

                    //set BroadcastReceiver to install app when .apk is downloaded
                    final String finalDestination = destination;
                    BroadcastReceiver onComplete = new BroadcastReceiver() {
                        public void onReceive(Context ctxt, Intent intent) {
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            vLog.debug(TAG, (new File(finalDestination)).toString());
                            install.setDataAndType(Uri.fromFile(new File(finalDestination)),
                                    "application/vnd.android.package-archive");
                            startActivity(install);

                            unregisterReceiver(this);
                            finish();
                        }
                    };
                    //register receiver for when .apk download is compete
                    registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }*/

    private void callUS() {
        Intent callUS = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+MyGlobal.getPhoneNumber()));
        startActivity(callUS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*Toast.makeText(HomeDrawerNavigationActivity.this, "Bấm trở lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
            if(doubleClickBackToExit) {
                super.onBackPressed();
                return;
            }
            doubleClickBackToExit = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClickBackToExit = false;
                }
            }, 2000);*/
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_main, menu);

        //define search menu
        final MenuItem mySearch = menu.findItem( R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) mySearch.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchView = new Intent(HomeDrawerNavigationActivity.this, SearchView.class);
                searchView.putExtra("q",query);
                searchView.putExtra("title","Tìm kiếm: "+query);
                startActivity(searchView);
                vLog.debug(TAG, "Search for query: "+ query);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    mySearch.collapseActionView();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//        //define share menu
//        getMenuInflater().inflate(R.menu.activity_home_drawer_navigation_drawer, menu);
//        MenuItem shareMenu = menu.getItem(R.id.nav_share);
//        mShareActionProvider = (ShareActionProvider) shareMenu.getActionProvider();


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
//            Intent searchBarView = new Intent(HomeDrawerNavigationActivity.this, SearchBarView.class);
//            startActivity(searchBarView);

            return true;
        }

        if (id == R.id.action_cartview) {
            Intent cartView = new Intent(HomeDrawerNavigationActivity.this, CartView.class);
            startActivity(cartView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_management) {
            if(MyGlobal.getUser()==null)
                switchFragment(new LoginFragment());
            else
                switchFragment(new AccountManagement());
            // Handle the camera action
        } else if (id == R.id.nav_home) {
            switchFragment(new MainFragment());
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_message));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_support) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+MyGlobal.getEmailTech()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT ,"Góp ý từ khách hàng");
            startActivity(Intent.createChooser(emailIntent, "Góp ý từ khách hàng"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = null;
        fmt = fm.beginTransaction().replace(R.id.frameLayoutNavContainer, fragment);
        fmt.commit();
    }
}
