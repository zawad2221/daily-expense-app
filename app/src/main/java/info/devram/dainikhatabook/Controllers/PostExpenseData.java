package info.devram.dainikhatabook.Controllers;

//import android.util.Log;

import java.util.HashMap;
import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;


public class PostExpenseData implements Runnable {
//    private static final String TAG = "PostExpenseData";

    private HashMap<String,String> setupRequest;
    private ResponseAvailableListener mListener;

    public PostExpenseData(HashMap<String, String> setupRequest,
                           ResponseAvailableListener listener) {
        this.setupRequest = setupRequest;
        this.mListener = listener;
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
        this.mListener.onExpenseResponse(converter.getExpenseArray(), statusCode);

    }

}
