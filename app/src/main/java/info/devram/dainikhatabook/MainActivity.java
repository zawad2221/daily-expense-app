package info.devram.dainikhatabook;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.Menu;
import android.view.MenuItem;

import info.devram.dainikhatabook.Adapters.MainActivityPagerAdapter;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

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

        final ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        FragmentStateAdapter viewPagerAdapter = new MainActivityPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                mainActivityViewModel
        );

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String[] tabTitles = new String[]{
                        "Today","Expenses","Income"
                };
                tab.setText(tabTitles[position]);
                viewPager.setCurrentItem(tab.getPosition(),true);
            }
        }).attach();



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