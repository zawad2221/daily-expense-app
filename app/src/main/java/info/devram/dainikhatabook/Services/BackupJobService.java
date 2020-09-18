package info.devram.dainikhatabook.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import info.devram.dainikhatabook.Controllers.Converter;
import info.devram.dainikhatabook.Controllers.PostAccountsData;
import info.devram.dainikhatabook.Controllers.RegisterUser;
import info.devram.dainikhatabook.Controllers.TokenRequest;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.ErrorHandlers.LogError;
import info.devram.dainikhatabook.Helpers.BackupServiceHelper;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.FileErrorLoggerListener;
import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;

import static android.Manifest.permission.READ_CONTACTS;

//import android.util.Log;

public class BackupJobService extends JobService {

    private static final String TAG = "BackupJobService";

    private AccountViewModel accountViewModel;
    private ExecutorService executorService;
    private JSONArray accountArr;
    //private JSONObject responseObject;
    private List<AccountEntity> syncObjectList;
    private String accountEmail = null;
    private String apiPassword;

    @Override
    public boolean onStartJob(JobParameters params) {
        accountViewModel = AccountViewModel.getInstance(getApplication());
        accountViewModel.init();
        parseData();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void parseData() {
//        int hasGetAccountPermission = ContextCompat.checkSelfPermission(
//                getApplicationContext(), READ_CONTACTS);
//
//        if (hasGetAccountPermission == PackageManager.PERMISSION_GRANTED) {
//            SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
//            accountEmail = preferences.getString("account",null);
//            if (accountEmail == null) {
//                NotifyService.createNotification("Backup Notification",
//                        "No account Found", getApplicationContext());
//                return;
//            }
//        }
        accountEmail = "rahulmalikcool@gmail.com";
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);


        apiPassword = sharedPreferences.getString("api_password", "");

        if (apiPassword == null || apiPassword.equals("")) {
            NotifyService.createNotification("Backup Notification",
                    "Password Not set. Kindly Do so in App Settings",
                    getApplicationContext());
            return;
        }

        List<String> loginData = new ArrayList<>();

        loginData.add(accountEmail);
        loginData.add(apiPassword);

        BackupServiceHelper helper =
                new BackupServiceHelper(getApplicationContext(), accountViewModel, loginData);

        helper.startBackup();

        SharedPreferences sp = getSharedPreferences("update", MODE_PRIVATE);

        Set<String> accountIDs = sp.getStringSet("accountIDs", new HashSet<>());

        assert accountIDs != null;
        helper.startUpdateBackup(accountIDs);

    }
}

