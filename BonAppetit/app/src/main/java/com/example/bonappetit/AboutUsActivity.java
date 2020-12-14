package com.example.bonappetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //bottom navigation

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById (R.id.bottomnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId ()) {
                    case R.id.account:
                        Intent accountintent=new Intent(getApplicationContext(),AccountActivity.class);
                        startActivity(accountintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
                        Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
                        startActivity(cartintent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;
                    default:
                        return false;

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

}