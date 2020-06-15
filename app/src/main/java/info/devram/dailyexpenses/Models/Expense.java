package info.devram.dailyexpenses.Models;

import android.database.sqlite.SQLiteDatabase;

import info.devram.dailyexpenses.Config.Util;

public class Expense {

    private int id;
    private String expenseType;
    private String expenseDate;
    private String expenseDesc;
    private int expenseAmount;

    public Expense(String expenseType, String expenseDesc,String expenseDate, int expenseAmount) {
        this.expenseType = expenseType;
        this.expenseDesc = expenseDesc;
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
    }

    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
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

    public static class Model implements ModelHandler {

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE IF NOT EXISTS " + Util.EXPENSE_TABLE_NAME + "("
                    + Util.EXPENSE_KEY_ID + " INTEGER PRIMARY KEY,"
                    + Util.EXPENSE_KEY_TYPE + " TEXT NOT NULL,"
                    + Util.EXPENSE_KEY_AMOUNT + " INTEGER NOT NULL,"
                    + Util.EXPENSE_KEY_DATE + " LONG NOT NULL,"
                    + Util.EXPENSE_KEY_DESC + " TEXT NOT NULL);";

            db.execSQL(query);
        }
    }
}
