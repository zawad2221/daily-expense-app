package info.devram.dainikhatabook.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Config.Util;
import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;

public class IncomeRepository implements DatabaseService<Income>  {

    private static final String TAG = "IncomeRepository";

    private DatabaseHandler db;
    private static IncomeRepository mInstance = null;
    private List<Income> incomeList;

    private IncomeRepository(Context context) {
        this.db = DatabaseHandler.getInstance(context);
        this.incomeList = new ArrayList<>();
    }

    public static IncomeRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new IncomeRepository(context);
        }
        return mInstance;
    }

    @Override
    public int addData(Income obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.INCOME_KEY_TYPE,obj.getIncomeType());
            contentValues.put(Util.INCOME_KEY_DESC,obj.getIncomeDesc());
            contentValues.put(Util.INCOME_KEY_AMOUNT,obj.getIncomeAmount());
            contentValues.put(Util.INCOME_KEY_DATE,obj.getIncomeDate());

            return (int)db.getWritableDatabase().insert(Util.INCOME_TABLE_NAME,
                    null,contentValues);
        }catch (SQLException e) {
            e.printStackTrace();

        }

        return obj.getId();
    }

    @Override
    public List<Income> getAll() {
        String query = "SELECT * FROM " + Util.INCOME_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {

                Income income = new Income();
                income.setId(cursor
                        .getInt(cursor.getColumnIndex(Util.INCOME_KEY_ID)));
                income.setIncomeType(cursor
                        .getString(cursor.getColumnIndex(Util.INCOME_KEY_TYPE)));
                income.setIncomeDesc(cursor
                        .getString(cursor.getColumnIndex(Util.INCOME_KEY_DESC)));
                income.setIncomeAmount(cursor
                        .getInt(cursor.getColumnIndex(Util.INCOME_KEY_AMOUNT)));
                income.setIncomeDate(cursor
                        .getString(cursor.getColumnIndex(Util.INCOME_KEY_DATE)));
                incomeList.add(income);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return incomeList;
    }

    @Override
    public Income getOne(int id) {
        Cursor cursor = db.getReadableDatabase().query(
                Util.INCOME_TABLE_NAME,new String[]{
                        Util.INCOME_KEY_ID,Util.INCOME_KEY_TYPE,Util.INCOME_KEY_DATE,
                        Util.INCOME_KEY_AMOUNT,Util.INCOME_KEY_DESC
                },Util.INCOME_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null
        );

        Income account = new Income();

        if (cursor != null) {
            cursor.moveToFirst();
            account.setId(cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
            account.setIncomeAmount(
                    cursor.getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
            account.setIncomeType(
                    cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
            account.setIncomeDate(
                    cursor.getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
            account.setIncomeDesc(
                    cursor.getColumnName(cursor.getColumnIndex(Util.EXPENSE_KEY_DESC)));
            cursor.close();
        }
        return account;
    }

    @Override
    public Boolean onUpdate(Income obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Util.INCOME_KEY_TYPE,obj.getIncomeType());
            contentValues.put(Util.INCOME_KEY_DESC,obj.getIncomeDesc());
            contentValues.put(Util.INCOME_KEY_AMOUNT,obj.getIncomeAmount());
            contentValues.put(Util.INCOME_KEY_DATE,obj.getIncomeDate());

            db.getWritableDatabase().update(Util.INCOME_TABLE_NAME,contentValues,
                    Util.INCOME_KEY_ID + "=?",
                    new String[]{String.valueOf(obj.getId())});

            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onUpdate: error " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean onDelete(Income obj) {
        try {
            db.getWritableDatabase().delete(Util.INCOME_TABLE_NAME,
                    Util.INCOME_KEY_ID + "=?",
                    new String[]{String.valueOf(obj.getId())});
            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onDelete: error " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCount() {
        String query = "SELECT * FROM " + Util.INCOME_TABLE_NAME;

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
