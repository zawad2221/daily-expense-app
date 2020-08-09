package info.devram.dainikhatabook.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    public GetRawData(GetRawData.OnDownloadListener mCallback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mCallback = mCallback;
    }

    public enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

    private DownloadStatus mDownloadStatus;
    private final OnDownloadListener mCallback;

    public interface OnDownloadListener {
        void onDownloadComplete(String data, DownloadStatus status);
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
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //int response = connection.getResponseCode();

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
