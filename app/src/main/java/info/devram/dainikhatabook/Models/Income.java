package info.devram.dainikhatabook.Models;

import android.database.sqlite.SQLiteDatabase;

import info.devram.dainikhatabook.Config.Util;

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

    public String getIncomeDesc() {
        return incomeDesc;
    }

    public void setIncomeDesc(String incomeDesc) {
        this.incomeDesc = incomeDesc;
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

    @Override
    public String toString() {
        String person = this.getIncomeType() + " " + this.getIncomeAmount();
        return person;
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
