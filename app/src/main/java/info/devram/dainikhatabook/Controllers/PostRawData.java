package info.devram.dainikhatabook.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingDeque;

public class PostRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "PostRawData";

    public enum UploadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

    private UploadStatus mUploadStatus;
    private final PostRawData.OnUploadListener mCallback;

    public interface OnUploadListener {
        void onUploadComplete(String data, UploadStatus status);

    }

    public PostRawData(OnUploadListener mCallback) {
        this.mUploadStatus = UploadStatus.IDLE;
        this.mCallback = mCallback;
    }


    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        if (mCallback != null) {
            mCallback.onUploadComplete(s, mUploadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader reader = null;
        String result = null;

        if (strings == null) {
            mUploadStatus = UploadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mUploadStatus = UploadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            bufferedWriter = new BufferedWriter(new
                    OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));

            bufferedWriter.write(strings[1]);
            bufferedWriter.flush();
            bufferedWriter.close();

            int response = connection.getResponseCode();
            if (response != 201) {
                Log.d(TAG, "doInBackground: error response " + response);

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));


                StringBuilder errorResult = new StringBuilder();
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    errorResult.append(line).append("\n");
                }
                result = String.valueOf(response);
                Log.d(TAG, "doInBackground: error result " + errorResult.toString());
                mUploadStatus = UploadStatus.FAILED_OR_EMPTY;
                return result;
            }

            Log.d(TAG, "doInBackground: success ");
            result = String.valueOf(response);
            Log.d(TAG, "doInBackground: " + result);
            mUploadStatus = UploadStatus.OK;
            return result;


        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: Error Reading Data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Error " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
