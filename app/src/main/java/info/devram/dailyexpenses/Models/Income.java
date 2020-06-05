package info.devram.dailyexpenses.Models;

import android.database.sqlite.SQLiteDatabase;

import info.devram.dailyexpenses.Config.Util;

public class Income {

    private int id;

    private String incomeType;
    private String incomeDate;
    private int incomeAmount;

    public Income(String incomeType, String incomeDate, int incomeAmount) {
        this.incomeType = incomeType;
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


}
