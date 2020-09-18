package info.devram.dainikhatabook.Interfaces;


import org.json.JSONArray;
import org.json.JSONObject;

import info.devram.dainikhatabook.Helpers.Util;

public interface ResponseAvailableListener {
    public void onTokenResponse(JSONObject jsonObject, int statusCode);

    public void onPostResponse(JSONObject message, int statusCode);

    public void onErrorResponse(String message, int statusCode);

    public void onRegisterResponse(JSONObject message, int statusCode);
}
