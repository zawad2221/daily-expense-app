package info.devram.dainikhatabook.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import info.devram.dainikhatabook.Controllers.Converter;
import info.devram.dainikhatabook.Controllers.PostAccountsData;
import info.devram.dainikhatabook.Controllers.TokenRequest;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.ErrorHandlers.LogError;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.FileErrorLoggerListener;
import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;

import static android.Manifest.permission.READ_CONTACTS;

//import android.util.Log;

public class BackupJobService extends JobService
        implements ResponseAvailableListener, FileErrorLoggerListener {



    private AccountViewModel accountViewModel;
    private ExecutorService executorService;
    private JSONArray accountArr;
    //private JSONObject responseObject;
    private List<AccountEntity> syncObjectList;


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
        int hasGetAccountPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(), READ_CONTACTS);
        String account = null;
        if (hasGetAccountPermission == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
            account = preferences.getString("account",null);
            if (account == null) {
                NotifyService.createNotification("Backup Notification",
                        "No account Found", getApplicationContext());
                return;
            }
        }

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);


        String apiPassword = sharedPreferences.getString("api_password", "");

        if (apiPassword == null || apiPassword.equals("")) {
            NotifyService.createNotification("Backup Notification",
                    "Password Not set. Kindly Do so in App Settings",
                    getApplicationContext());
            return;
        }

        syncObjectList = accountViewModel.getAccountForSyncing();
        if (syncObjectList.size() > 0) {

            Converter converter = new Converter();
            converter.setRequestData(syncObjectList);
            converter.setFromObject(true);
            converter.run();

            accountArr = converter.getJsonArray();

            executorService = Executors.newCachedThreadPool();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("url", Config.LOGIN_URL);
            hashMap.put("email", account);
            hashMap.put("password", apiPassword);
            TokenRequest tokenRequest = new TokenRequest(hashMap, this);

            if (syncObjectList.size() > 0) {

                executorService.execute(tokenRequest);
            }
        }
    }



    @Override
    public void onTokenResponse(JSONObject jsonObject, int statusCode) {
        if (statusCode != 200) {
            shutdownExecutor();
            NotifyService.createNotification("Backup",
                    "Error while syncing data", getApplicationContext());
        }
        if (!executorService.isShutdown()) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (accountArr.length() > 0) {
                hashMap.put("url", Config.ACCOUNT_URL);
                try {

                    hashMap.put("token", jsonObject.getJSONObject("msg").getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hashMap.put("data", accountArr.toString());
                PostAccountsData postAccountsData = new PostAccountsData(hashMap, this);
                executorService.execute(postAccountsData);
            }

        }
    }

    @Override
    public void onPostResponse(JSONObject message, int statusCode) {
        if (statusCode == 201) {
            this.accountViewModel.updateAccountAfterSynced(syncObjectList);
        }
    }

    @Override
    public void onErrorResponse(String message, int statusCode) {
        if (statusCode == 503) {
            try {
                throw new ApplicationError(message);
            } catch (ApplicationError error) {
                LogError logError = new LogError(error, getApplicationContext(), this);
                executorService.execute(logError);
            }
        }
    }


    private void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }

    @Override
    public void fileStatusListener(String status) {
        executorService.shutdown();
    }
}

