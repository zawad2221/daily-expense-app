package info.devram.dainikhatabook.Controllers;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;

public class RegisterUser implements Runnable
{
    private static final String TAG = "RegisterUser";

    private HashMap<String, String> setupRequest;
    private ResponseAvailableListener mListener;

    public RegisterUser(HashMap<String, String> setupRequest, ResponseAvailableListener mListener) {
        this.setupRequest = setupRequest;
        this.mListener = mListener;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: starts");
        HttpURLConnection connection = null;
        BufferedReader reader;
        Converter converter = new Converter();
        converter.setFromString(true);

        if (setupRequest == null) {
            return;
        }

        try {
            String userCredentials = setupRequest.get("email") + ":" + setupRequest.get("password");

            byte[] encodedCredentials = Base64.encode(userCredentials.getBytes(), Base64.DEFAULT);
            String basicAuth = "Basic " + new String(encodedCredentials);
            URL url = new URL(setupRequest.get("url"));

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setConnectTimeout(8000);
            connection.setDoOutput(true);

            int response = connection.getResponseCode();
            Log.d(TAG, "run: " + response);
            if (response != 200) {

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String errorResult = stringBuilder(reader);

                this.mListener.onErrorResponse(errorResult, response);
                return;
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String okResult = stringBuilder(reader);

            Log.d(TAG, "run: " + okResult);

            converter.setStringData(okResult);
            converter.run();
            JSONObject jsonObject = converter.getJsonObject();
            this.mListener.onRegisterResponse(jsonObject, response);

        } catch (MalformedURLException | SocketTimeoutException | SecurityException e) {
            this.mListener.onErrorResponse(e.getMessage(), 503);
        } catch (IOException e) {
            e.printStackTrace();
            this.mListener.onErrorResponse(e.getMessage(), 503);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "run: ends");
    }

    private String stringBuilder(BufferedReader reader) {
        StringBuilder result = new StringBuilder();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
