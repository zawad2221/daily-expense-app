package info.devram.dainikhatabook.Interfaces;

import org.json.JSONArray;

public interface ResponseAvailableListener {
    public void onTokenResponse(JSONArray jsonArray,int statusCode);

    public void onExpenseResponse(JSONArray jsonArray,int statusCode);

    public void onIncomeResponse(JSONArray jsonArray,int statusCode);
}
