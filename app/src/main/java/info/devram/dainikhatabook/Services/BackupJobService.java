package info.devram.dainikhatabook.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;

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
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class BackupJobService extends JobService implements ResponseAvailableListener {

    //private static final String TAG = "BackupJobService";

    private MainActivityViewModel mainActivityViewModel = new MainActivityViewModel(getBaseContext());
    private ExecutorService executorService;
    private JSONArray expArr;
    private JSONArray incArr;
    private List<DashBoardObject> syncObjectList;


    @Override
    public boolean onStartJob(JobParameters params) {
        mainActivityViewModel.init();
        parseData();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void parseData() {
        syncObjectList = mainActivityViewModel.getDataForSyncing();
        if (syncObjectList.size() > 0) {
            Converter converter = new Converter();
            converter.setRequestData(syncObjectList);
            converter.setFromObject(true);
            converter.run();
            expArr = converter.getExpenseArray();
            incArr = converter.getIncomeArray();

            if (expArr.length() > 0 || incArr.length() > 0) {
                executorService = Executors.newCachedThreadPool();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("url", Config.LOGIN_URL);
                hashMap.put("email", "rahulmalikcool@gmail.com");
                TokenRequest tokenRequest = new TokenRequest(hashMap, this);
                executorService.execute(tokenRequest);
            }
        }

    }

    @Override
    public void onTokenResponse(JSONArray jsonArray, int statusCode) {
        if (statusCode != 200) {
            shutdownExecutor();
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
            mainActivityViewModel.updateSyncListWithDb(dashBoardObjects);
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
            mainActivityViewModel.updateSyncListWithDb(dashBoardObjects);
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

//    private void getUserAccount() {
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
//        }
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS}, 0);
//        }
//
//        AccountManager am = AccountManager.get(this);
//
//        Account[] accounts = am.getAccountsByType("com.google");
//        Log.d(TAG, "getUserAccount: " + accounts.length);
//
//    }
}
