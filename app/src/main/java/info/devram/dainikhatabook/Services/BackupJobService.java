package info.devram.dainikhatabook.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
//import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import info.devram.dainikhatabook.Controllers.Converter;
import info.devram.dainikhatabook.Controllers.PostExpenseData;
import info.devram.dainikhatabook.Controllers.PostIncomeData;
import info.devram.dainikhatabook.Controllers.TokenRequest;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;

import static android.Manifest.permission.READ_CONTACTS;

public class BackupJobService extends JobService implements ResponseAvailableListener {

    private static final String TAG = "BackupJobService";

    private AccountViewModel accountViewModel = new AccountViewModel(getBaseContext());
    private ExecutorService executorService;
    private JSONArray expArr;
    private JSONArray incArr;
    private List<DashBoardObject> syncObjectList;
    public static final String PRIMARY_CHANNEL_ID = "account_channel";

    @Override
    public boolean onStartJob(JobParameters params) {
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
            Log.d(TAG, "parseData: " + account);
            if (account == null) {
                createNotification("Backup Notification","No account Found");
                return;
            }
        }

        syncObjectList = accountViewModel.getDataForSyncing();
        if (syncObjectList.size() > 0) {
            Converter converter = new Converter();
            converter.setRequestData(syncObjectList);
            converter.setFromObject(true);
            converter.run();
            expArr = converter.getExpenseArray();
            incArr = converter.getIncomeArray();

            if (syncObjectList.size() > 0) {
                executorService = Executors.newCachedThreadPool();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("url", Config.LOGIN_URL);
                hashMap.put("email", account);
                TokenRequest tokenRequest = new TokenRequest(hashMap, this);
                executorService.execute(tokenRequest);
            }
        }
    }

    private void createNotification(String title,String message) {
        NotificationManager mNotifyManager;
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(),PRIMARY_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setColor(getApplicationContext().getColor(R.color.accentSecondary));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            title,
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (message);
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
        mNotifyManager.notify(0,builder.build());
    }

    @Override
    public void onTokenResponse(JSONArray jsonArray, int statusCode) {
        if (statusCode != 200) {
            shutdownExecutor();
            createNotification("Backup","Error while syncing data");
        }
        if (!executorService.isShutdown()) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (expArr.length() > 0) {
                hashMap.put("url", Config.EXPENSE_URL);
                try {
                    hashMap.put("token", jsonArray.getJSONObject(0).getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hashMap.put("data", expArr.toString());
                PostExpenseData postExpenseData = new PostExpenseData(hashMap, this);
                executorService.execute(postExpenseData);
            }
            if (incArr.length() > 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("url", Config.INCOME_URL);
                try {
                    map.put("token", jsonArray.getJSONObject(0).getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("data", incArr.toString());
                PostIncomeData postIncomeData = new PostIncomeData(map, this);
                executorService.execute(postIncomeData);
            }
        }
    }

    @Override
    public void onExpenseResponse(JSONArray jsonArray, int statusCode) {
        shutdownExecutor();
        if (statusCode == 201) {
            List<DashBoardObject> dashBoardObjects = new ArrayList<>();
            for (int i = 0; i < syncObjectList.size(); i++) {
                if (syncObjectList.get(i).getIsExpense()) {
                    dashBoardObjects.add(syncObjectList.get(i));
                }
            }
            accountViewModel.updateSyncListWithDb(dashBoardObjects);
        }

    }

    @Override
    public void onIncomeResponse(JSONArray jsonArray, int statusCode) {
        if (statusCode == 201) {
            List<DashBoardObject> dashBoardObjects = new ArrayList<>();
            for (int i = 0; i < syncObjectList.size(); i++) {
                if (!syncObjectList.get(i).getIsExpense()) {
                    dashBoardObjects.add(syncObjectList.get(i));
                }
            }
            accountViewModel.updateSyncListWithDb(dashBoardObjects);
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
}
