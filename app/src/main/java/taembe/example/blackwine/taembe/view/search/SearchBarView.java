package taembe.example.blackwine.taembe.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import taembe.example.blackwine.taembe.R;
import taembe.example.blackwine.taembe.common.vLog;

public class SearchBarView extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    android.support.v7.widget.SearchView searchView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar_view);

        init();
    }

    private void init() {
        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.searchViewBar);
        searchView.onActionViewExpanded();
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                Intent searchView = new Intent(SearchBarView.this, taembe.example.blackwine.taembe.view.search.SearchView.class);
                searchView.putExtra("q",query);
                searchView.putExtra("title","Tìm kiếm: "+query);
                startActivity(searchView);
                vLog.debug(TAG, "Search for query: "+ query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
