package info.devram.dainikhatabook.Mappers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.MapperInterface;
import info.devram.dainikhatabook.Models.Expense;

public class ExpenseMapper{

    private static final String TAG = "ExpenseRepository";

    private DatabaseHandler db;
    private static ExpenseMapper mInstance = null;
    private List<Expense> expenseList;

    private ExpenseMapper(Context context) {
        this.db = DatabaseHandler.getInstance(context);

    }

    public static ExpenseMapper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ExpenseMapper(context);
        }
        return mInstance;
    }


    public void addData(Expense obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Config.EXPENSE_KEY_ID,obj.getId());
            contentValues.put(Config.EXPENSE_KEY_TYPE,obj.getExpenseType());
            contentValues.put(Config.EXPENSE_KEY_DESC,obj.getExpenseDesc());
            contentValues.put(Config.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
            contentValues.put(Config.EXPENSE_KEY_DATE,obj.getExpenseDate());

            db.getWritableDatabase().insert(Config.EXPENSE_TABLE_NAME,
                    null,contentValues);
        }catch (SQLException e) {
            Log.e(TAG, "addData: error " + e.getMessage());

        }

    }


    public List<Expense> getAll() {

        this.expenseList = new ArrayList<>();

        String query = "SELECT * FROM " + Config.EXPENSE_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {

                Expense expense = new Expense();
                expense.setId(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_ID)));
                expense.setExpenseType(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
                expense.setExpenseDesc(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_DESC)));
                expense.setExpenseAmount(cursor
                        .getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
                expense.setExpenseDate(cursor
                        .getLong(cursor.getColumnIndex(Config.EXPENSE_KEY_DATE)));
                int boolValue = cursor.getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_SYNC));
                if (boolValue == 0) {
                    expense.setSyncStatus(false);
                }else expense.setSyncStatus(true);
                expenseList.add(expense);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return expenseList;
    }

    public List<Expense> getSummaryByType(List<String> types) {
        this.expenseList = new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {

            try (Cursor cursor = db.getReadableDatabase().rawQuery("SELECT "
                            + Config.EXPENSE_KEY_TYPE + ", SUM("
                            + Config.EXPENSE_KEY_AMOUNT + ") as " + Config.EXPENSE_KEY_AMOUNT + " FROM "
                            + Config.EXPENSE_TABLE_NAME + " WHERE " + Config.EXPENSE_KEY_TYPE + "=?",
                    new String[]{types.get(i)})) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)) != 0) {
                        Expense expense = new Expense();
                        expense.setExpenseType(cursor.getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
                        expense.setExpenseAmount(cursor.getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
                        expenseList.add(expense);
                    }
                }
            }
        }
        return expenseList;
    }


    public Expense getOne(int id) {
        Cursor cursor = db.getReadableDatabase().query(
                Config.EXPENSE_TABLE_NAME,new String[]{
                        Config.EXPENSE_KEY_ID, Config.EXPENSE_KEY_TYPE, Config.EXPENSE_KEY_DATE,
                        Config.EXPENSE_KEY_AMOUNT, Config.EXPENSE_KEY_DESC
                }, Config.EXPENSE_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null
        );

        Expense account = new Expense();

        if (cursor != null) {
            cursor.moveToFirst();
            account.setId(cursor.getString(cursor.getColumnIndex(Config.EXPENSE_KEY_ID)));
            account.setExpenseAmount(
                    cursor.getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
            account.setExpenseType(
                    cursor.getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
            account.setExpenseDate(
                    cursor.getLong(cursor.getColumnIndex(Config.EXPENSE_KEY_DATE)));
            account.setExpenseDesc(
                    cursor.getColumnName(cursor.getColumnIndex(Config.EXPENSE_KEY_DESC)));
            cursor.close();
        }
        return account;
    }


    public Boolean onUpdate(Expense obj) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Config.EXPENSE_KEY_TYPE,obj.getExpenseType());
            contentValues.put(Config.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
            contentValues.put(Config.EXPENSE_KEY_DATE,obj.getExpenseDate());
            contentValues.put(Config.EXPENSE_KEY_DESC,obj.getExpenseDesc());

            db.getWritableDatabase().update(Config.EXPENSE_TABLE_NAME,contentValues,
                    Config.EXPENSE_KEY_ID + "=?",new String[]{String.valueOf(obj.getId())});

            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onUpdate: error " + e.getMessage());
            return false;
        }


    }


    public Boolean onDelete(Expense obj) {
        try {
            db.getWritableDatabase().delete(Config.EXPENSE_TABLE_NAME,
                    Config.EXPENSE_KEY_ID + "=?",
                    new String[]{String.valueOf(obj.getId())});
            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onDelete: error " + e.getMessage());
            return false;
        }
    }


    public int getCount() {
        String query = "SELECT * FROM " + Config.EXPENSE_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void updateSync(List<Expense> expenseList) {
        SQLiteDatabase updateDB = db.getWritableDatabase();
        updateDB.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Expense expense: expenseList) {
                values.put(Config.EXPENSE_KEY_SYNC, Config.SYNC_STATUS_TRUE);
                updateDB.update(Config.EXPENSE_TABLE_NAME,values,
                        Config.EXPENSE_KEY_ID + "=?",
                        new String[]{String.valueOf(expense.getId())});
            }
            updateDB.setTransactionSuccessful();

        }catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "updateSync: " + e.getMessage());
        }finally {
            updateDB.endTransaction();
        }

    }

//    public List<Expense> getByDate() {
//        expenseList = new ArrayList<>();
//        Cursor cursor = db.getReadableDatabase().query(
//                Config.EXPENSE_TABLE_NAME,new String[]{Config.EXPENSE_KEY_ID, Config.EXPENSE_KEY_TYPE,
//                Config.EXPENSE_KEY_DATE, Config.EXPENSE_KEY_AMOUNT},null,null,
//                null,null, Config.EXPENSE_KEY_DATE + " ASC");
//
//        if(cursor.moveToFirst()) {
//            do {
//                Expense expense = new Expense();
//                expense.setId(cursor
//                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_ID)));
//                expense.setExpenseType(cursor
//                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
//
//                expense.setExpenseAmount(cursor
//                        .getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
//                expense.setExpenseDate(cursor
//                        .getLong(cursor.getColumnIndex(Config.EXPENSE_KEY_DATE)));
//                expenseList.add(expense);
//            }while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return expenseList;
//    }

    public List<Expense> getByType(String expenseType) {
        this.expenseList = new ArrayList<>();

        try (Cursor cursor = db.getReadableDatabase().query(Config.EXPENSE_TABLE_NAME,
                new String[]{Config.EXPENSE_KEY_ID, Config.EXPENSE_KEY_DATE,
                        Config.EXPENSE_KEY_AMOUNT, Config.EXPENSE_KEY_DESC,
                        Config.EXPENSE_KEY_TYPE}, Config.EXPENSE_KEY_TYPE + "=?",
                new String[]{expenseType}, null, null, null)) {
            while (cursor.moveToNext()) {
                Expense expense = new Expense();
                expense.setId(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_ID)));
                expense.setExpenseType(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
                expense.setExpenseDesc(cursor
                        .getString(cursor.getColumnIndex(Config.EXPENSE_KEY_DESC)));
                expense.setExpenseAmount(cursor
                        .getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
                expense.setExpenseDate(cursor
                        .getLong(cursor.getColumnIndex(Config.EXPENSE_KEY_DATE)));
                expenseList.add(expense);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getByType: " + e.getMessage());
        }
        return expenseList;
    }

}