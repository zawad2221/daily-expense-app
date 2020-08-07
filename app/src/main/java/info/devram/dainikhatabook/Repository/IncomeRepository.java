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

import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Models.Income;

public class IncomeRepository implements DatabaseService<Income>  {

    private static final String TAG = "IncomeRepository";

    private DatabaseHandler db;
    private static IncomeRepository mInstance = null;
    private List<Income> incomeList;

    private IncomeRepository(Context context) {
        this.db = DatabaseHandler.getInstance(context);

    }

    public static IncomeRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new IncomeRepository(context);
        }
        return mInstance;
    }

    @Override
    public void addData(Income obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Config.EXPENSE_KEY_ID,obj.getId());
            contentValues.put(Config.INCOME_KEY_TYPE,obj.getIncomeType());
            contentValues.put(Config.INCOME_KEY_DESC,obj.getIncomeDesc());
            contentValues.put(Config.INCOME_KEY_AMOUNT,obj.getIncomeAmount());
            contentValues.put(Config.INCOME_KEY_DATE,obj.getIncomeDate());

            db.getWritableDatabase().insert(Config.INCOME_TABLE_NAME,
                    null,contentValues);
        }catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<Income> getAll() {
        this.incomeList = new ArrayList<>();
        String query = "SELECT * FROM " + Config.INCOME_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {

                Income income = new Income();
                income.setId(cursor
                        .getString(cursor.getColumnIndex(Config.INCOME_KEY_ID)));
                income.setIncomeType(cursor
                        .getString(cursor.getColumnIndex(Config.INCOME_KEY_TYPE)));
                income.setIncomeDesc(cursor
                        .getString(cursor.getColumnIndex(Config.INCOME_KEY_DESC)));
                income.setIncomeAmount(cursor
                        .getInt(cursor.getColumnIndex(Config.INCOME_KEY_AMOUNT)));
                income.setIncomeDate(cursor
                        .getLong(cursor.getColumnIndex(Config.INCOME_KEY_DATE)));
                int boolValue = cursor.getInt(cursor.getColumnIndex(Config.INCOME_KEY_SYNC));
                if (boolValue == 0) {
                    income.setSyncStatus(false);
                }else income.setSyncStatus(true);
                incomeList.add(income);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return incomeList;
    }

    @Override
    public Income getOne(int id) {
        Cursor cursor = db.getReadableDatabase().query(
                Config.INCOME_TABLE_NAME,new String[]{
                        Config.INCOME_KEY_ID, Config.INCOME_KEY_TYPE, Config.INCOME_KEY_DATE,
                        Config.INCOME_KEY_AMOUNT, Config.INCOME_KEY_DESC
                }, Config.INCOME_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null
        );

        Income account = new Income();

        if (cursor != null) {
            cursor.moveToFirst();
            account.setId(cursor.getString(cursor.getColumnIndex(Config.EXPENSE_KEY_ID)));
            account.setIncomeAmount(
                    cursor.getInt(cursor.getColumnIndex(Config.EXPENSE_KEY_AMOUNT)));
            account.setIncomeType(
                    cursor.getString(cursor.getColumnIndex(Config.EXPENSE_KEY_TYPE)));
            account.setIncomeDate(
                    cursor.getLong(cursor.getColumnIndex(Config.EXPENSE_KEY_DATE)));
            account.setIncomeDesc(
                    cursor.getColumnName(cursor.getColumnIndex(Config.EXPENSE_KEY_DESC)));
            cursor.close();
        }
        return account;
    }

    @Override
    public Boolean onUpdate(Income obj) {
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Config.INCOME_KEY_TYPE,obj.getIncomeType());
            contentValues.put(Config.INCOME_KEY_DESC,obj.getIncomeDesc());
            contentValues.put(Config.INCOME_KEY_AMOUNT,obj.getIncomeAmount());
            contentValues.put(Config.INCOME_KEY_DATE,obj.getIncomeDate());

            db.getWritableDatabase().update(Config.INCOME_TABLE_NAME,contentValues,
                    Config.INCOME_KEY_ID + "=?",
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
            db.getWritableDatabase().delete(Config.INCOME_TABLE_NAME,
                    Config.INCOME_KEY_ID + "=?",
                    new String[]{String.valueOf(obj.getId())});
            return true;
        }catch (SQLException e) {
            Log.e(TAG, "onDelete: error " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCount() {
        String query = "SELECT * FROM " + Config.INCOME_TABLE_NAME;

        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void updateSync(List<Income> incomeList) {

        SQLiteDatabase updateDB = db.getWritableDatabase();
        updateDB.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Income income: incomeList) {
                values.put(Config.INCOME_KEY_SYNC, Config.SYNC_STATUS_TRUE);
                updateDB.update(Config.INCOME_TABLE_NAME,values,
                        Config.INCOME_KEY_ID + "=?",
                        new String[]{income.getId()});
            }
            updateDB.setTransactionSuccessful();

        }catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "updateSync: " + e.getMessage());
        }finally {
            updateDB.endTransaction();
        }
    }

    public List<Income> getByDate() {
        incomeList = new ArrayList<>();
        Cursor cursor = db.getReadableDatabase()
                .query(Config.INCOME_TABLE_NAME,new String[]{Config.INCOME_KEY_ID,
                Config.INCOME_KEY_TYPE, Config.INCOME_KEY_AMOUNT, Config.INCOME_KEY_DATE},
                        null,null,null,
                        null, Config.INCOME_KEY_DATE + " ASC");
        if(cursor.moveToFirst()) {
            do {

                Income income = new Income();
                income.setId(cursor
                        .getString(cursor.getColumnIndex(Config.INCOME_KEY_ID)));
                income.setIncomeType(cursor
                        .getString(cursor.getColumnIndex(Config.INCOME_KEY_TYPE)));
                income.setIncomeAmount(cursor
                        .getInt(cursor.getColumnIndex(Config.INCOME_KEY_AMOUNT)));
                income.setIncomeDate(cursor
                        .getLong(cursor.getColumnIndex(Config.INCOME_KEY_DATE)));
                incomeList.add(income);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return incomeList;
    }

//    private String getDate() {
//        Calendar myCalendar = Calendar.getInstance();
//        String myFormat = "dd/MM/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);
//
//        return sdf.format(myCalendar.getTime());
//    }
}
