package info.devram.dainikhatabook.Models;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import info.devram.dainikhatabook.Helpers.Config;

public class Income implements Serializable {

    //private static final String TAG = "Income";
    public static final long serialVersionUID = 20200801L;

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + Config.INCOME_TABLE_NAME + "("
                    + Config.INCOME_KEY_ID + " TEXT NOT NULL,"
                    + Config.INCOME_KEY_TYPE + " TEXT NOT NULL,"
                    + Config.INCOME_KEY_AMOUNT + " INTEGER NOT NULL,"
                    + Config.INCOME_KEY_DATE + " LONG NOT NULL,"
                    + Config.INCOME_KEY_DESC + " TEXT NOT NULL,"
                    + Config.INCOME_KEY_SYNC + " INTEGER DEFAULT "
                    + Config.SYNC_STATUS_FALSE + ");";

            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    db.execSQL("CREATE TEMP TABLE inc_temp (id TEXT NOT NULL, type TEXT NOT NULL," +
                            " amount INTEGER NOT NULL, date_added LONG NOT NULL, " +
                            "description TEXT NOT NULL);");
                    db.execSQL("INSERT INTO inc_temp SELECT * FROM income");

                    db.execSQL("DROP TABLE income");
                    String query = "CREATE TABLE IF NOT EXISTS " + Config.INCOME_TABLE_NAME + "("
                            + Config.INCOME_KEY_ID + " TEXT NOT NULL,"
                            + Config.INCOME_KEY_TYPE + " TEXT NOT NULL,"
                            + Config.INCOME_KEY_AMOUNT + " INTEGER NOT NULL,"
                            + Config.INCOME_KEY_DATE + " LONG NOT NULL,"
                            + Config.INCOME_KEY_DESC + " TEXT NOT NULL,"
                            + Config.INCOME_KEY_SYNC + " INTEGER DEFAULT "
                            + Config.SYNC_STATUS_FALSE + ");";
                    db.execSQL(query);
                    db.execSQL("INSERT INTO " + Config.INCOME_TABLE_NAME + "("
                            + Config.INCOME_KEY_ID + "," + Config.INCOME_KEY_TYPE + ","
                            + Config.INCOME_KEY_AMOUNT + "," + Config.INCOME_KEY_DATE + ","
                            + Config.INCOME_KEY_DESC + ") SELECT "
                            + "id,type,amount,date_added,description FROM inc_temp;");
                    db.execSQL("DROP TABLE inc_temp");
                    break;

            }
        }
    }

}
