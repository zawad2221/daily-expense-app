package info.devram.dainikhatabook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import info.devram.dainikhatabook.ui.ConfirmModal;

import static android.Manifest.permission.READ_CONTACTS;

public class SettingsActivity extends AppCompatActivity {

    //private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setTitle("App Settings");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements ConfirmModal.ConfirmModalListener, Preference.OnPreferenceChangeListener {

        private Context context;
        private Activity activity;
        private SwitchPreferenceCompat backupSwitch;
        private int hasGetAccountPermission;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            context = getContext();
            activity = getActivity();
            backupSwitch = findPreference("backup");
            EditTextPreference passwordEditText = findPreference("api_password");

            if (backupSwitch != null) {
                backupSwitch.setOnPreferenceChangeListener(this);
            }

            if (passwordEditText != null) {
                passwordEditText.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                    }
                });
                passwordEditText.setOnPreferenceChangeListener(this);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            hasGetAccountPermission = ContextCompat.checkSelfPermission(
                    context, READ_CONTACTS);
            if (hasGetAccountPermission == PackageManager.PERMISSION_GRANTED) {
                backupSwitch.setChecked(true);
            }
        }

        private boolean setAccountPermission() {
            boolean check = ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_CONTACTS);

            hasGetAccountPermission = ContextCompat.checkSelfPermission(
                    context, READ_CONTACTS);

            if (hasGetAccountPermission == PackageManager.PERMISSION_GRANTED) {
                return setContactPermission();
            } else if (check) {

                ConfirmModal confirmModal = new ConfirmModal(
                        "For Backup Service to be enabled " +
                                "this app needs to have access for reading " +
                                "contacts stored in this phone",
                        "Information!\n", true, this);
                confirmModal.show(getParentFragmentManager(), null);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{READ_CONTACTS}, 1);
            }
            return false;
        }

        private boolean setContactPermission() {

            int hasContactReadPermission = ContextCompat.checkSelfPermission(
                    context, READ_CONTACTS);

            if (hasContactReadPermission == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                if (ActivityCompat
                        .shouldShowRequestPermissionRationale(
                                activity, READ_CONTACTS)) {

                    ActivityCompat.requestPermissions(
                            activity,
                            new String[]{READ_CONTACTS}, 1);
                }
                return false;
            }
        }

        @Override
        public void onOkClick(DialogFragment dialogFragment) {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            );
            Uri uri = Uri.fromParts("package",
                    activity.getPackageName(), null);

            intent.setData(uri);
            activity.startActivity(intent);
        }

        @Override
        public void onCancelClick(DialogFragment dialogFragment) {
            dialogFragment.dismiss();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            if (preference.getKey().equalsIgnoreCase("api_password")) {
                return newValue != "";
            }

            boolean isBackEnabled = (Boolean) newValue;

            if (isBackEnabled) {
                if (!setAccountPermission()) {
                    backupSwitch.setChecked(false);
                    return false;
                }
            }
            return true;
        }
    }
}