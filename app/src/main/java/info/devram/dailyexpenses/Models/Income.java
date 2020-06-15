package info.devram.dailyexpenses.Models;

import android.database.sqlite.SQLiteDatabase;

import info.devram.dailyexpenses.Config.Util;

public class Income {

    private int id;
    private String incomeType;
    private String incomeDesc;
    private String incomeDate;
    private int incomeAmount;

    public Income(String incomeType, String incomeDesc,String incomeDate, int incomeAmount) {
        this.incomeType = incomeType;
        this.incomeDesc = incomeDesc;
        this.incomeDate = incomeDate;
        this.incomeAmount = incomeAmount;
    }

    public Income() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public int getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(int incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + Util.INCOME_TABLE_NAME + "("
                    + Util.INCOME_KEY_ID + " INTEGER PRIMARY KEY,"
                    + Util.INCOME_KEY_TYPE + " TEXT NOT NULL,"
                    + Util.INCOME_KEY_AMOUNT + " INTEGER NOT NULL,"
                    + Util.INCOME_KEY_DATE + " LONG NOT NULL,"
                    + Util.INCOME_KEY_DESC + " TEXT NOT NULL);";

            db.execSQL(query);
        }
    }

}
