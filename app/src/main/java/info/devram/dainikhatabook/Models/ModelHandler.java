package info.devram.dainikhatabook.Models;

import android.database.sqlite.SQLiteDatabase;

public interface ModelHandler {

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
