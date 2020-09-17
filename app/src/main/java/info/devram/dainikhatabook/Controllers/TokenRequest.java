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
        BufferedReader reader;
        Converter converter = new Converter();
        converter.setFromString(true);

        if (setupRequest == null) {
            return;
        }

        try {
            String userCredentials = setupRequest.get("email") + ":" + setupRequest.get("password");

            byte[] encodedCredentials = Base64.encode(userCredentials.getBytes(),Base64.DEFAULT);
            String basicAuth = "Basic " + new String(encodedCredentials);
            URL url = new URL(setupRequest.get("url"));

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setConnectTimeout(8000);
            connection.setDoOutput(true);

            int response = connection.getResponseCode();
            if (response != 200) {

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String errorResult = stringBuilder(reader);
                converter.setStringData(errorResult);
                converter.run();

                this.mListener.onTokenResponse(converter.getJsonObject(), response);
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String okResult = stringBuilder(reader);
            converter.setStringData(okResult);
            converter.run();
            this.mListener.onTokenResponse(converter.getJsonObject(), response);
            converter = null;
        } catch (MalformedURLException e) {
            Log.e(TAG, "run Invalid URL: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            converter.setStringData("{'msg':'error connecting to server'}");
            converter.run();
        } catch (IOException e) {
            this.mListener.onErrorResponse(e.getMessage(), 503);
        } catch (SecurityException e) {
            Log.e(TAG, "run Security Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
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
