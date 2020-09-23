package info.devram.dainikhatabook.Controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.JsonParser;


public class Converter implements JsonParser<AccountEntity> {

    //private static final String TAG = "Converter";

    private List<AccountEntity> requestData;
    private String stringData;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private boolean isFromObject = false;
    private boolean isFromString = false;
    //private boolean isToObject = false;

    public void setRequestData(List<AccountEntity> requestData) {
        this.requestData = requestData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public void setFromObject(boolean fromObject) {
        isFromObject = fromObject;
    }

    public void setFromString(boolean fromString) {
        isFromString = fromString;
    }

    @Override
    public void parseFromObject(List<AccountEntity> object) {
        JSONObject jsonObject;
        jsonArray = new JSONArray();
        try {
            for (int i = 0; i < object.size(); i++) {
                jsonObject = new JSONObject();
                jsonObject.put("id", object.get(i).accountID.getId());
                jsonObject.put("type", object.get(i).accountType.getType());
                jsonObject.put("amount", object.get(i).accountMoney.getAmount());
                jsonObject.put("date", object.get(i).accountCreatedDate.getCreatedAt());
                jsonObject.put("description", object.get(i).accountDescription.getDesc());
                if (object.get(i).accountRepoType.getRepoType().equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                    jsonObject.put("repo", "expenses");
                } else {
                    jsonObject.put("repo", "incomes");
                }
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject parseFromString(String data) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<AccountEntity> parseToObject(JSONArray jsonArray) {
        return null;
    }


    public void run() {
        if (isFromObject) {
            parseFromObject(this.requestData);
        }
        if (isFromString) {
            jsonObject = parseFromString(this.stringData);
        }
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
