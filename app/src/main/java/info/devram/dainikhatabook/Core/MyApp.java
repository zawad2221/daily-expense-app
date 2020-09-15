package info.devram.dainikhatabook.Core;

import android.app.Application;
import android.content.Context;


public class MyApp extends Application {

    private static MyApp mInstance;

    private MyApp() {

    }

    public static synchronized MyApp getInstance() {
        if (mInstance == null) {
            mInstance = new MyApp();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
