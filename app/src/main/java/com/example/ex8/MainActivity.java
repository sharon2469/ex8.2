package com.example.ex8;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

// Note that mainActivity is empty because all the work done inside the fragment
public class MainActivity extends AppCompatActivity implements CountriesAdapter.ICountriesAdapterListener{

    CountriesAdapter countriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.logo);
        menu.setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_exit, menu);
        inflater.inflate(R.menu.world_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.exit:
                MyExit newFragment = MyExit.newInstance();
                newFragment.show(getSupportFragmentManager(), "exitDialog");
                break;
            case R.id.worldSettings:
                //countriesAdapter.reloadCounteyList(this);
                break;
        }

        return  true;
    }

    @Override
    public void countryClicked() {
        DetailsFragment fragB;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragB = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        else //I am in portrait
        {
            fragB = new DetailsFragment();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.mainActivity, fragB).//add on top of the static fragment
                    addToBackStack("BBB").//cause the back button scrolling through the loaded fragments
                    commit();
            getSupportFragmentManager().executePendingTransactions();
        }

    }
}
