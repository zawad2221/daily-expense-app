package info.devram.dailyexpenses;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;


import info.devram.dailyexpenses.Adapters.ViewPagerAdapter;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;
import info.devram.dailyexpenses.ui.IncomePageFragment;
import info.devram.dailyexpenses.ui.SaveDataDialog;
import info.devram.dailyexpenses.ui.TodayPageFragment;
import info.devram.dailyexpenses.ui.ExpensePageFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivityViewModel mainActivityViewModel;
        mainActivityViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication()).create(MainActivityViewModel.class);

        mainActivityViewModel.init();

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new TodayPageFragment(mainActivityViewModel),"Today");
        viewPagerAdapter.addFragment(new ExpensePageFragment(mainActivityViewModel),"Expenses");
        viewPagerAdapter.addFragment(new IncomePageFragment(mainActivityViewModel),"Income");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}