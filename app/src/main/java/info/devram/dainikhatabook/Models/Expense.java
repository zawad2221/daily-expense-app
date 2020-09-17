package info.devram.dainikhatabook.Models;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import info.devram.dainikhatabook.Helpers.Config;

public class Expense implements Serializable {
    //private static final String TAG = "Expense";
    private static final long serialVersionUID = 20200725L;

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + Config.EXPENSE_TABLE_NAME + "("
                    + Config.EXPENSE_KEY_ID + " TEXT NOT NULL,"
                    + Config.EXPENSE_KEY_TYPE + " TEXT NOT NULL,"
                    + Config.EXPENSE_KEY_AMOUNT + " INTEGER NOT NULL,"
                    + Config.EXPENSE_KEY_DATE + " LONG NOT NULL,"
                    + Config.EXPENSE_KEY_DESC + " TEXT NOT NULL,"
                    + Config.EXPENSE_KEY_SYNC + " INTEGER DEFAULT " + Config.SYNC_STATUS_FALSE + ");";


            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            switch (oldVersion) {
                case 1:
                    db.execSQL("CREATE TEMP TABLE exp_temp (id TEXT NOT NULL, type TEXT NOT NULL," +
                            " amount INTEGER NOT NULL, date_added LONG NOT NULL, description TEXT NOT NULL);");
                    db.execSQL("INSERT INTO exp_temp SELECT * FROM expense");

                    db.execSQL("DROP TABLE expense");
                    String query = "CREATE TABLE IF NOT EXISTS " + Config.EXPENSE_TABLE_NAME + "("
                            + Config.EXPENSE_KEY_ID + " TEXT NOT NULL,"
                            + Config.EXPENSE_KEY_TYPE + " TEXT NOT NULL,"
                            + Config.EXPENSE_KEY_AMOUNT + " INTEGER NOT NULL,"
                            + Config.EXPENSE_KEY_DATE + " LONG NOT NULL,"
                            + Config.EXPENSE_KEY_DESC + " TEXT NOT NULL,"
                            + Config.EXPENSE_KEY_SYNC + " INTEGER DEFAULT " + Config.SYNC_STATUS_FALSE + ");";
                    db.execSQL(query);
                    db.execSQL("INSERT INTO " + Config.EXPENSE_TABLE_NAME + "("
                            + Config.EXPENSE_KEY_ID + "," + Config.EXPENSE_KEY_TYPE + ","
                            + Config.EXPENSE_KEY_AMOUNT + "," + Config.EXPENSE_KEY_DATE + ","
                            + Config.EXPENSE_KEY_DESC + ") SELECT id,type,amount,date_added,description FROM exp_temp;");
                    db.execSQL("DROP TABLE exp_temp");
                    break;

            }
        }
    }
}
