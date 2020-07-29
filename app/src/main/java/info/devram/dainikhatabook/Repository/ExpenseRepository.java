package info.devram.dainikhatabook.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Config.Util;
import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Models.Expense;

public class ExpenseRepository implements DatabaseService<Expense> {

    private static final String TAG = "ExpenseRepository";

    private DatabaseHandler db;
    private static ExpenseRepository mInstance = null;
    private List<Expense> expenseList;

    private ExpenseRepository(Context context) {
        this.db = DatabaseHandler.getInstance(context);

    }

    public static ExpenseRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ExpenseRepository(context);
        }
        return mInstance;
    }

    @Override
    public void addData(Expense obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.EXPENSE_KEY_ID,obj.getId());
            contentValues.put(Util.EXPENSE_KEY_TYPE,obj.getExpenseType());
            contentValues.put(Util.EXPENSE_KEY_DESC,obj.getExpenseDesc());
            contentValues.put(Util.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
            contentValues.put(Util.EXPENSE_KEY_DATE,obj.getExpenseDate());

            db.getWritableDatabase().insert(Util.EXPENSE_TABLE_NAME,
                    null,contentValues);
        }catch (SQLException e) {
            Log.e(TAG, "addData: error " + e.getMessage());

        }

    }

    @Override
    public List<Expense> getAll() {

        this.expenseList = new ArrayList<>();

        String query = "SELECT * FROM " + Util.EXPENSE_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {

                Expense expense = new Expense();
                expense.setId(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
                expense.setExpenseType(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
                expense.setExpenseDesc(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DESC)));
                expense.setExpenseAmount(cursor
                        .getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
                expense.setExpenseDate(cursor
                        .getLong(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
                int boolValue = cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_SYNC));
                if (boolValue == 0) {
                    expense.setSyncStatus(false);
                }else expense.setSyncStatus(true);
                expenseList.add(expense);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return expenseList;
    }

    @Override
    public Expense getOne(int id) {
        Cursor cursor = db.getReadableDatabase().query(
                Util.EXPENSE_TABLE_NAME,new String[]{
                        Util.EXPENSE_KEY_ID,Util.EXPENSE_KEY_TYPE,Util.EXPENSE_KEY_DATE,
                        Util.EXPENSE_KEY_AMOUNT,Util.EXPENSE_KEY_DESC
                },Util.EXPENSE_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null
        );

        Expense account = new Expense();

        if (cursor != null) {
            cursor.moveToFirst();
            account.setId(cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
            account.setExpenseAmount(
                    cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
            account.setExpenseType(
                    cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
            account.setExpenseDate(
                    cursor.getLong(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
            account.setExpenseDesc(
                    cursor.getColumnName(cursor.getColumnIndex(Util.EXPENSE_KEY_DESC)));
            cursor.close();
        }
        return account;
    }

    @Override
    public Boolean onUpdate(Expense obj) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.EXPENSE_KEY_TYPE,obj.getExpenseType());
            contentValues.put(Util.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
            contentValues.put(Util.EXPENSE_KEY_DATE,obj.getExpenseDate());
            contentValues.put(Util.EXPENSE_KEY_DESC,obj.getExpenseDesc());

            db.getWritableDatabase().update(Util.EXPENSE_TABLE_NAME,contentValues,
                    Util.EXPENSE_KEY_ID + "=?",new String[]{String.valueOf(obj.getId())});

            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onUpdate: error " + e.getMessage());
            return false;
        }


    }

    @Override
    public Boolean onDelete(Expense obj) {
        try {
            db.getWritableDatabase().delete(Util.EXPENSE_TABLE_NAME,
                    Util.EXPENSE_KEY_ID + "=?",
                    new String[]{String.valueOf(obj.getId())});
            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onDelete: error " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCount() {
        String query = "SELECT * FROM " + Util.EXPENSE_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void updateSync(List<Expense> expenseList) {
        Log.d(TAG, "updateSync: starts");
        SQLiteDatabase updateDB = db.getWritableDatabase();
        updateDB.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Expense expense: expenseList) {
                values.put(Util.EXPENSE_KEY_SYNC,Util.SYNC_STATUS_TRUE);
                updateDB.update(Util.EXPENSE_TABLE_NAME,values,
                        Util.EXPENSE_KEY_ID + "=?",
                        new String[]{String.valueOf(expense.getId())});
            }
            updateDB.setTransactionSuccessful();
            Log.d(TAG, "updateSync: ends");
        }catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "updateSync: " + e.getMessage());
        }finally {
            Log.d(TAG, "updateSync: end transaction");
            updateDB.endTransaction();
        }

    }
//    private String getDate() {
//        Calendar myCalendar = Calendar.getInstance();
//        String myFormat = "dd/MM/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);
//
//        return sdf.format(myCalendar.getTime());
//    }
}
