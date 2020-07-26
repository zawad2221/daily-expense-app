package info.devram.dainikhatabook.Repository;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "PostRawData";

    public enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

    private DownloadStatus mDownloadStatus;
    private final PostRawData.OnDownloadListener mCallback;

    public interface OnDownloadListener {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public PostRawData(OnDownloadListener mCallback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mCallback = mCallback;
    }


    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        if (mCallback != null) {
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{'type':'clothing','date':'45600','amount':'550','description':'test'}";

            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code " + response);


            bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            bufferedWriter.write(jsonInputString);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }


            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Error Reading Data " + e.getMessage());
        }catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Error " + e.getMessage());
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                }catch (IOException e) {
                    Log.e(TAG, "doInBackground: " + e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }catch (IOException e) {
                    Log.e(TAG, "doInBackground: " + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
