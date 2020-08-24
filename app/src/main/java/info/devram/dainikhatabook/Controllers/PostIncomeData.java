package info.devram.dainikhatabook.Controllers;

//import android.util.Log;
import java.util.HashMap;

import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;

public class PostIncomeData implements Runnable {

//    private static final String TAG = "PostIncomeData";

    private HashMap<String,String> setupRequest;
    private ResponseAvailableListener mListener;

    public PostIncomeData(HashMap<String, String> setupRequest,
                          ResponseAvailableListener mListener) {
        this.setupRequest = setupRequest;
        this.mListener = mListener;
    }

    @Override
    public void run() {
        Converter converter = new Converter();
        converter.setFromString(true);
        PostData postData = new PostData(setupRequest);
        String response = postData.postRequest();
        int statusCode = postData.getResponseStatus();
        converter.setStringData(response);
        converter.run();
        this.mListener.onIncomeResponse(converter.getIncomeArray(), statusCode);
    }
}
