package info.devram.dainikhatabook.Controllers;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

import info.devram.dainikhatabook.Interfaces.ResponseAvailableListener;

public class TokenRequest implements Runnable {

    private static final String TAG = "TokenRequest";

    private HashMap<String,String> setupRequest;
    private ResponseAvailableListener mListener;

    public TokenRequest(HashMap<String,String> request,ResponseAvailableListener listener) {
        this.setupRequest = request;
        this.mListener = listener;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        Converter converter = new Converter();
        converter.setFromString(true);

        if (setupRequest == null) {
            return;
        }

        try {
            String userCredentials = setupRequest.get("email") + ":" + setupRequest.get("ipAddress");
            byte[] encodedCredentials = Base64.encode(userCredentials.getBytes(),Base64.DEFAULT);
            String basicAuth = "Basic " + new String(encodedCredentials);
            URL url = new URL(setupRequest.get("url"));

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setConnectTimeout(8000);
            connection.connect();

            int response = connection.getResponseCode();
            if (response != 200) {

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String errorResult = stringBuilder(reader);
                converter.setStringData(errorResult);
                converter.run();

                this.mListener.onTokenResponse(converter.getJsonArray(), response);
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String okResult = stringBuilder(reader);
            converter.setStringData(okResult);
            converter.run();
            this.mListener.onTokenResponse(converter.getJsonArray(), response);
            converter = null;
        } catch (MalformedURLException e) {
            Log.i(TAG, "run Invalid URL: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            converter.setStringData("{'msg':'error connecting to server'}");
            converter.run();
        } catch (IOException e) {
            Log.i(TAG, "run Error Reading Data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.i(TAG, "run Security Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.i(TAG, "run: " + e.getMessage());
                }
            }
        }
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
