package info.devram.dainikhatabook.Controllers;

//import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import info.devram.dainikhatabook.Controllers.PostRawData.UploadStatus;
import info.devram.dainikhatabook.Models.Expense;

public class PostJsonData implements
        PostRawData.OnUploadListener, JsonParser<Expense> {

    //private static final String TAG = "PostJsonData";
    private String mBaseUrl;

    private final OnReponseAvailableListener listener;
    private Boolean isRunOnSameThread = false;

    public interface OnReponseAvailableListener {
        void onResponseAvailable(String httpMessage, UploadStatus status);
    }

    public PostJsonData(OnReponseAvailableListener listener, String url) {
        this.mBaseUrl = url;
        this.listener = listener;

    }

    @Override
    public void onUploadComplete(String data, PostRawData.UploadStatus status) {
        listener.onResponseAvailable(data, status);
    }

    @Override
    public void parseJson(List<Expense> object) {
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < object.size(); i++) {
                jsonObject = new JSONObject();
                jsonObject.put("id", object.get(i).getId());
                jsonObject.put("type", object.get(i).getExpenseType());
                jsonObject.put("amount", object.get(i).getExpenseAmount());
                jsonObject.put("date", object.get(i).getExpenseDate());
                jsonObject.put("description", object.get(i).getExpenseDesc());
                jsonArray.put(i,jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isRunOnSameThread) {
            String[] params = {mBaseUrl, jsonArray.toString()};
            executeOnSameThread(params);
        }

    }

    public void executeOnSameThread(String[] searchCriteria) {

        isRunOnSameThread = true;
        PostRawData postRawData = new PostRawData(this);
        postRawData.execute(searchCriteria);

    }
}
