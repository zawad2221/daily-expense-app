package info.devram.dainikhatabook;

import android.os.Bundle;
//import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


import info.devram.dainikhatabook.Services.SyncService;

public class SettingsActivity extends AppCompatActivity {

    //private static final String TAG = "SettingsActivity";
    public static String aboutSummary = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        aboutSummary = String.format(getResources().getString(R.string.about_summary),
                BuildConfig.VERSION_NAME);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SyncService syncService;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference about = findPreference("about");
            about.setSummary(aboutSummary);

        }

    }
}