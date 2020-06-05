package info.devram.dailyexpenses.Models;

import android.database.sqlite.SQLiteDatabase;

public interface ModelHandler {

    public abstract void onCreate(SQLiteDatabase db);
}
