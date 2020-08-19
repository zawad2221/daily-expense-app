package info.devram.dainikhatabook.Controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.devram.dainikhatabook.Interfaces.JsonParser;
import info.devram.dainikhatabook.Models.DashBoardObject;


public class Converter implements JsonParser<DashBoardObject> {

    //private static final String TAG = "Converter";

    private List<DashBoardObject> requestData;
    private String stringData;
    private JSONArray expenseArray;
    private JSONArray incomeArray;
    private JSONArray jsonArray;
    private boolean isFromObject = false;
    private boolean isFromString = false;
    //private boolean isToObject = false;

    public void setRequestData(List<DashBoardObject> requestData) {
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
    public void parseFromObject(List<DashBoardObject> object) {
        JSONObject jsonObject;
        expenseArray = new JSONArray();
        incomeArray = new JSONArray();
        try {
            for (int i = 0; i < object.size(); i++) {
                jsonObject = new JSONObject();
                jsonObject.put("id", object.get(i).getIdObject());
                jsonObject.put("type", object.get(i).getTypeObject());
                jsonObject.put("amount", object.get(i).getAmountObject());
                jsonObject.put("date", object.get(i).getDateObject());
                jsonObject.put("description", object.get(i).getDescObject());
                if (object.get(i).getIsExpense()) {
                    expenseArray.put(jsonObject);
                }else {
                    incomeArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONArray parseFromString(String data) {
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject = new JSONObject(data);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    public List<DashBoardObject> parseToObject(Object data) {
        return null;
    }


    public void run() {
        if (isFromObject) {
            parseFromObject(this.requestData);
        }
        if (isFromString) {
            jsonArray = parseFromString(this.stringData);
        }
    }

    public JSONArray getExpenseArray() {
        return expenseArray;
    }

    public JSONArray getIncomeArray() {
        return incomeArray;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
