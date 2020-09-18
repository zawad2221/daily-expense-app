package info.devram.dainikhatabook.Helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
import info.devram.dainikhatabook.Interfaces.FileErrorLoggerListener;
import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;
import info.devram.dainikhatabook.Services.NotifyService;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;


public class BackupServiceHelper implements ResponseAvailableListener, FileErrorLoggerListener
{
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


    public void startBackup()
    {
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
            TokenRequest tokenRequest = new TokenRequest(hashMap, this);

            if (accountEntityList.size() > 0) {
                executorService.execute(tokenRequest);
            }
        }

    }

    public void startUpdateBackup(Set<String> strings)
    {
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
    public void onTokenResponse(JSONObject jsonObject, int statusCode) {
        Log.d(TAG, "onTokenResponse: starts");
        try {
            Log.d(TAG, "run: " + jsonObject.getJSONObject("msg"));
            Log.d(TAG, "run: " + statusCode);
            if (statusCode == 404) {
                if (jsonObject.getJSONObject("msg").has("Create New")) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("url", Config.REGISTER_URL);
                    hashMap.put("email", mLoginData.get(0));
                    hashMap.put("password", mLoginData.get(1));
                    RegisterUser registerUser = new RegisterUser(hashMap, this);
                    executorService.execute(registerUser);
                    Log.d(TAG, "onTokenResponse: register");
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onTokenResponse: debug register");
        if (statusCode != 200) {
            shutdownExecutor();
            NotifyService.createNotification("Backup",
                    "Error while syncing data", mContext);
        }

        this.sendDataSync(jsonObject);
    }

    private void sendDataSync(JSONObject jsonObject)
    {
        Log.d(TAG, "sendDataSync: starts");
        Log.d(TAG, "sendDataSync: " + executorService.isShutdown());
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
        Log.d(TAG, "onPostResponse: starts");
        if (statusCode == 201) {
            this.accountViewModel.updateAccountAfterSynced(accountEntityList);
        }
        Log.d(TAG, "onPostResponse: ends");
    }

    @Override
    public void onErrorResponse(String message, int statusCode) {
        if (statusCode == 503) {
            try {
                throw new ApplicationError(message);
            } catch (ApplicationError error) {
                LogError logError = new LogError(error, mContext, this);
                if (executorService.isShutdown()) {
                    executorService = Executors.newCachedThreadPool();
                    executorService.execute(logError);
                }

            }
        }
    }

    @Override
    public void onRegisterResponse(JSONObject message, int statusCode) {
        Log.d(TAG, "onRegisterResponse: " + message);
        Log.d(TAG, "onRegisterResponse: " + statusCode);
        if (statusCode == 200) {
            this.sendDataSync(message);
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
