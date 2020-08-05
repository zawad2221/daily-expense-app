package info.devram.dainikhatabook.Models;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import info.devram.dainikhatabook.Config.Util;

public class Income implements Serializable {

    private static final String TAG = "Income";
    public static final long serialVersionUID = 20200801L;

    private String id;
    private String incomeType;
    private String incomeDesc;
    private long incomeDate;
    private int incomeAmount;
    private boolean syncStatus;

    public Income(String incomeType, String incomeDesc,long incomeDate, int incomeAmount) {
        this.incomeType = incomeType;
        this.incomeDesc = incomeDesc;
        this.incomeDate = incomeDate;
        this.incomeAmount = incomeAmount;
    }

    public Income() {
    }

    public String getIncomeDesc() {
        return incomeDesc;
    }

    public void setIncomeDesc(String incomeDesc) {
        this.incomeDesc = incomeDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public long getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(long incomeDate) {
        this.incomeDate = incomeDate;
    }

    public int getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(int incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public boolean getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id='" + id + '\'' +
                ", incomeType='" + incomeType + '\'' +
                ", incomeDesc='" + incomeDesc + '\'' +
                ", incomeDate=" + incomeDate +
                ", incomeAmount=" + incomeAmount +
                ", syncStatus=" + syncStatus +
                '}';
    }

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + Util.INCOME_TABLE_NAME + "("
                    + Util.INCOME_KEY_ID + " TEXT NOT NULL,"
                    + Util.INCOME_KEY_TYPE + " TEXT NOT NULL,"
                    + Util.INCOME_KEY_AMOUNT + " INTEGER NOT NULL,"
                    + Util.INCOME_KEY_DATE + " LONG NOT NULL,"
                    + Util.INCOME_KEY_DESC + " TEXT NOT NULL,"
                    + Util.INCOME_KEY_SYNC + " INTEGER DEFAULT "
                    + Util.SYNC_STATUS_FALSE + ");";

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
                    String query = "CREATE TABLE IF NOT EXISTS " + Util.INCOME_TABLE_NAME + "("
                            + Util.INCOME_KEY_ID + " TEXT NOT NULL,"
                            + Util.INCOME_KEY_TYPE + " TEXT NOT NULL,"
                            + Util.INCOME_KEY_AMOUNT + " INTEGER NOT NULL,"
                            + Util.INCOME_KEY_DATE + " LONG NOT NULL,"
                            + Util.INCOME_KEY_DESC + " TEXT NOT NULL,"
                            + Util.INCOME_KEY_SYNC + " INTEGER DEFAULT "
                            + Util.SYNC_STATUS_FALSE + ");";
                    db.execSQL(query);
                    db.execSQL("INSERT INTO " + Util.INCOME_TABLE_NAME + "("
                            + Util.INCOME_KEY_ID + "," + Util.INCOME_KEY_TYPE + ","
                            + Util.INCOME_KEY_AMOUNT + "," + Util.INCOME_KEY_DATE + ","
                            + Util.INCOME_KEY_DESC + ") SELECT "
                            + "id,type,amount,date_added,description FROM inc_temp;");
                    db.execSQL("DROP TABLE inc_temp");
                    break;

            }
        }
    }

}
