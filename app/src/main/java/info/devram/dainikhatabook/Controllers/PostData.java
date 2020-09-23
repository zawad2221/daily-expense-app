package info.devram.dainikhatabook.Controllers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.Interfaces.ResponseListener;
import info.devram.dainikhatabook.enums.RequestType;
import info.devram.dainikhatabook.enums.RequestURI;

public class PostData {

    private static final String TAG = "PostData";
    private final RequestType requestType;
    private final ResponseListener mListener;
    private int responseCode;
    private HashMap<String, String> setupRequest;

    public PostData(RequestType request, ResponseListener listener) {
        this.requestType = request;
        this.mListener = listener;
    }

    public String postRequest(RequestURI requestURI) throws ApplicationError {
        Log.d(TAG, "postRequest: starts");
        HttpURLConnection connection = null;
        BufferedWriter bufferedWriter;
        BufferedReader reader;
        Converter converter = new Converter();
        converter.setFromString(true);

        if (setupRequest == null) {
            throw new ApplicationError("request data not sent", getClass().getName());
        }

        try {
            URL url = new URL(setupRequest.get("url"));

            connection = (HttpURLConnection) url.openConnection();

            if (requestType == RequestType.POST) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("PUT");
            }

            if (requestURI == RequestURI.ACCOUNTS) {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + setupRequest.get("token"));
                connection.setRequestProperty("Accept", "application/json");
            } else {
                String userCredentials =
                        setupRequest.get("email") + ":" + setupRequest.get("password");

                String encodedCredentials =
                        Base64.getEncoder().encodeToString(userCredentials.getBytes());
                String basicAuth = "Basic " + encodedCredentials;
                connection.setRequestProperty("Authorization", basicAuth);
            }

            connection.setDoOutput(true);

            if (setupRequest.containsKey("data")) {
                bufferedWriter = new BufferedWriter(new
                        OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));

                bufferedWriter.write(setupRequest.get("data"));
                bufferedWriter.flush();
                bufferedWriter.close();
            }

            responseCode = connection.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                Log.d(TAG, "postRequest: " + responseCode);
                Log.d(TAG, "postRequest: " + connection.getInputStream());

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String okResult = stringBuilder(reader);
                Log.d(TAG, "postRequest: " + okResult);

                converter.setStringData(okResult);
                converter.run();

                JSONObject jsonObject = converter.getJsonObject();

                if (requestURI == RequestURI.ACCOUNTS) {
                    this.mListener.onPostResponse(jsonObject, responseCode);

                    return null;
                } else {
                    okResult = this.mListener.onTokenResponse(jsonObject, responseCode);

                    return okResult;
                }

            }

            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            String errorResult = stringBuilder(reader);

            converter.setStringData(errorResult);
            converter.run();

            JSONObject jsonObject = converter.getJsonObject();

            if (requestURI == RequestURI.ACCOUNTS) {
                this.mListener.onErrorResponse(errorResult, responseCode);
                Log.d(TAG, "postRequest: error response " + jsonObject);
                return null;
            }

            if (responseCode == 404) {
                errorResult = this.mListener.onTokenResponse(jsonObject, responseCode);
                this.mListener.onLoginFailure(jsonObject, responseCode);
                Log.d(TAG, "postRequest: " + errorResult);
                throw new ApplicationError(errorResult, getClass().getName());
            }
            Log.d(TAG, "postRequest: ends");
            return null;

        } catch (SecurityException | IOException e) {
            throw new ApplicationError(e.getMessage(), Arrays.toString(e.getStackTrace()));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            converter = null;
        }
    }

    public int getResponseStatus() {
        return responseCode;
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

    public void setSetupRequest(HashMap<String, String> setupRequest) {
        this.setupRequest = setupRequest;
    }
}
