package info.devram.dainikhatabook.Interfaces;


import org.json.JSONArray;
import org.json.JSONObject;

import info.devram.dainikhatabook.Helpers.Util;

public interface ResponseListener {
    public String onTokenResponse(JSONObject jsonObject, int statusCode);

    public JSONArray getRequestData();

    public void onPostResponse(JSONObject message, int statusCode);

    public void onErrorResponse(String message, int statusCode);

    public void onLoginFailure(JSONObject message, int statusCode);
}
