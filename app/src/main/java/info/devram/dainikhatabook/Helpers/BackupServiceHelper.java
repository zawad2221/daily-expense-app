package info.devram.dainikhatabook.Helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.devram.dainikhatabook.Controllers.APIRequest;
import info.devram.dainikhatabook.Controllers.Converter;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.LogError;
import info.devram.dainikhatabook.Interfaces.FileErrorLoggerListener;
import info.devram.dainikhatabook.Interfaces.ResponseListener;
import info.devram.dainikhatabook.Services.NotifyService;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;


public class BackupServiceHelper implements ResponseListener, FileErrorLoggerListener {
    private static final String TAG = "BackupServiceHelper";
    private Context mContext;
    private ExecutorService executorService;
    private JSONArray accountArr;
    private List<AccountEntity> accountEntityList;
    private AccountViewModel accountViewModel;
    private List<String> mLoginData;

    public BackupServiceHelper(Context mContext, AccountViewModel accountViewModel,
                               List<String> loginData) {
        this.mContext = mContext;
        this.accountViewModel = accountViewModel;
        this.mLoginData = loginData;
    }


    public void startBackup() {
        this.accountEntityList = this.accountViewModel.getAccountForSyncing();
        if (accountEntityList.size() > 0) {
            Converter converter = new Converter();
            converter.setRequestData(accountEntityList);
            converter.setFromObject(true);
            converter.run();

            accountArr = converter.getJsonArray();

            executorService = Executors.newCachedThreadPool();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("url", Config.LOGIN_URL);
            hashMap.put("email", mLoginData.get(0));
            hashMap.put("password", mLoginData.get(1));
            APIRequest APIRequest = new APIRequest(hashMap, this);

            if (accountEntityList.size() > 0) {
                executorService.execute(APIRequest);
            }
        }

    }

    public void startUpdateBackup(Set<String> strings) {
        if (strings.size() == 0) {
            return;
        }

        List<AccountID> accountIDS = new ArrayList<>();
        for (String id : strings) {
            AccountID accountID = new AccountID(id);
            accountIDS.add(accountID);
        }

        if (accountIDS.size() > 0) {
            List<AccountEntity> accounts = this.accountViewModel.backupAccountAfterUpdate(accountIDS);
        }

//        UpdateAccount updateAccount = new UpdateAccount();
//
//        executorService.submit()
    }

    @Override
    public String onTokenResponse(JSONObject jsonObject, int statusCode) {

        if (statusCode == 200) {
            try {
                return jsonObject.getJSONObject("msg").getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (statusCode == 404) {
            try {
                if (jsonObject.getJSONObject("msg").has("Create New")) {
                    return jsonObject.getJSONObject("msg").getString("Create New");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            shutdownExecutor();
//            NotifyService.createNotification("Backup",
//                    "Error while syncing data", mContext);
        }

        return null;
    }

    @Override
    public JSONArray getRequestData() {
        if (this.accountArr != null) {
            return accountArr;
        }
        return null;
    }

    @Override
    public void onPostResponse(JSONObject message, int statusCode) {

        if (statusCode == 201) {
            this.accountViewModel.updateAccountAfterSynced(accountEntityList);
        }
        executorService.shutdownNow();

    }

    @Override
    public void onErrorResponse(String message, int statusCode) {
        executorService.shutdownNow();
        if (statusCode == 503) {

            NotifyService.createNotification("Backup",
                    "Error while syncing data", mContext);

            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("error", message);
            hashMap.put("trace", getClass().getName());

            LogError logError = new LogError(hashMap, mContext, this);
            if (executorService.isShutdown()) {
                executorService = Executors.newCachedThreadPool();
                executorService.execute(logError);
            }

        }
    }

    @Override
    public void onLoginFailure(JSONObject message, int statusCode) {
        executorService.shutdownNow();
        if (executorService.isShutdown()) {
            executorService = Executors.newCachedThreadPool();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("url", Config.REGISTER_URL);
            hashMap.put("email", mLoginData.get(0));
            hashMap.put("password", mLoginData.get(1));
            APIRequest tokenRequest = new APIRequest(hashMap, this);

            executorService.execute(tokenRequest);
        }
    }

    @Override
    public void fileStatusListener(String status) {
        executorService.shutdown();
    }

}
