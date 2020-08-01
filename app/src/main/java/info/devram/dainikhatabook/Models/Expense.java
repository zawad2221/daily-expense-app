package info.devram.dainikhatabook.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;

import info.devram.dainikhatabook.Config.Util;

public class Expense implements Serializable {
    private static final String TAG = "Expense";
    private static final long serialVersionUID = 20200725L;

    private String id;
    private String expenseType;
    private long expenseDate;
    private String expenseDesc;
    private int expenseAmount;
    private boolean syncStatus;

    public boolean getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Expense(String expenseType, String expenseDesc, long expenseDate, int expenseAmount) {
        this.expenseType = expenseType;
        this.expenseDesc = expenseDesc;
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
    }

    public Expense() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public long getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(long expenseDate) {
        this.expenseDate = expenseDate;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseDesc() {
        return expenseDesc;
    }

    public void setExpenseDesc(String expenseDesc) {
        this.expenseDesc = expenseDesc;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", expenseDate=" + expenseDate +
                ", expenseDesc='" + expenseDesc + '\'' +
                ", expenseAmount=" + expenseAmount +
                ", syncStatus=" + syncStatus +
                '}';
    }

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE expense(id TEXT NOT NULL," +
                    "type TEXT NOT NULL,amount INTEGER NOT NULL," +
                    "date_added LONG NOT NULL,description TEXT NOT NULL);";


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
                    String query = "CREATE TABLE IF NOT EXISTS " + Util.EXPENSE_TABLE_NAME + "("
                            + Util.EXPENSE_KEY_ID + " TEXT NOT NULL,"
                            + Util.EXPENSE_KEY_TYPE + " TEXT NOT NULL,"
                            + Util.EXPENSE_KEY_AMOUNT + " INTEGER NOT NULL,"
                            + Util.EXPENSE_KEY_DATE + " LONG NOT NULL,"
                            + Util.EXPENSE_KEY_DESC + " TEXT NOT NULL,"
                            + Util.EXPENSE_KEY_SYNC + " INTEGER DEFAULT " + Util.SYNC_STATUS_FALSE + ");";
                    db.execSQL(query);
                    db.execSQL("INSERT INTO " + Util.EXPENSE_TABLE_NAME + "("
                            + Util.EXPENSE_KEY_ID + "," + Util.EXPENSE_KEY_TYPE + ","
                            + Util.EXPENSE_KEY_AMOUNT + "," + Util.EXPENSE_KEY_DATE + ","
                            + Util.EXPENSE_KEY_DESC + ") SELECT id,type,amount,date_added,description FROM exp_temp;");
                    db.execSQL("DROP TABLE exp_temp");
                    break;

            }
        }
    }
}
