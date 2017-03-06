package taembe.example.blackwine.taembe.view.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import taembe.example.blackwine.taembe.R;

public class CategoryView extends AppCompatActivity {
    Button btn;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        btn = (Button) findViewById(R.id.btn);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Start");


        Intent i = new Intent(CategoryView.this, CategoryView.class);
        startActivity(i);
    }
}
