package info.devram.dainikhatabook.Controllers;

import android.util.Log;

import org.json.JSONArray;

import java.util.HashMap;

import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.ResponseListener;
import info.devram.dainikhatabook.enums.RequestType;
import info.devram.dainikhatabook.enums.RequestURI;

public class APIRequest implements Runnable {

    private static final String TAG = "APIRequest";

    private HashMap<String, String> setupRequest;
    private ResponseListener mListener;

    public APIRequest(HashMap<String, String> request, ResponseListener listener) {
        this.setupRequest = request;
        this.mListener = listener;
    }

    @Override
    public void run()
    {
        Log.d(TAG, "run: starts");
        PostData postData =
                new PostData(RequestType.POST, mListener);

        try {
            postData.setSetupRequest(setupRequest);

            String result = postData.postRequest(RequestURI.LOGIN);

            if (postData.getResponseStatus() == 404 &&
                    result.equalsIgnoreCase(Config.REGISTER_URL)) {
                return;
            }

            if (result != null) {
                JSONArray jsonArray = this.mListener.getRequestData();

                setupRequest = new HashMap<>();

                setupRequest.put("url", Config.ACCOUNT_URL);
                setupRequest.put("token", result);
                setupRequest.put("data", jsonArray.toString());

                postData.setSetupRequest(setupRequest);

                postData.postRequest(RequestURI.ACCOUNTS);
            }

        } catch (ApplicationError error) {
            this.mListener.onErrorResponse(error.getMessage(), 503);
        }
        Log.d(TAG, "run: ends");
    }
}
