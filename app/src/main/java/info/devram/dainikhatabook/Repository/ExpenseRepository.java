package info.devram.dainikhatabook.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.util.Log;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;
//import java.util.Locale;

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
        this.expenseList = new ArrayList<>();
    }

    public static ExpenseRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ExpenseRepository(context);
        }
        return mInstance;
    }

    @Override
    public int addData(Expense obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.EXPENSE_KEY_TYPE,obj.getExpenseType());
            contentValues.put(Util.EXPENSE_KEY_DESC,obj.getExpenseDesc());
            contentValues.put(Util.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
            contentValues.put(Util.EXPENSE_KEY_DATE,obj.getExpenseDate());

            return (int)db.getWritableDatabase().insert(Util.EXPENSE_TABLE_NAME,
                    null,contentValues);
        }catch (SQLException e) {
            Log.e(TAG, "addData: error " + e.getMessage());
            return -1;
        }

    }

    @Override
    public List<Expense> getAll() {

        String query = "SELECT * FROM " + Util.EXPENSE_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {

                Expense expense = new Expense();
                expense.setId(cursor
                        .getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
                expense.setExpenseType(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
                expense.setExpenseDesc(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DESC)));
                expense.setExpenseAmount(cursor
                        .getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
                expense.setExpenseDate(cursor
                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
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
            account.setId(cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
            account.setExpenseAmount(
                    cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
            account.setExpenseType(
                    cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
            account.setExpenseDate(
                    cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
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

//    private String getDate() {
//        Calendar myCalendar = Calendar.getInstance();
//        String myFormat = "dd/MM/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);
//
//        return sdf.format(myCalendar.getTime());
//    }
}
