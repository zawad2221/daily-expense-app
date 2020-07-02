package info.devram.dainikhatabook.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import info.devram.dainikhatabook.Config.Util;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Models.ModelHandler;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static DatabaseHandler mInstance = null;
    private ModelHandler expenseModel = new Expense.Model();
    private ModelHandler incomeModel = new Income.Model();
    private SQLiteDatabase sqLiteDatabase;

    private DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATBASE_NAME, null, Util.DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            return new DatabaseHandler(context);
        }
        return mInstance;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        sqLiteDatabase = super.getReadableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        sqLiteDatabase =  super.getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        expenseModel.onCreate(db);
        incomeModel.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        super.close();
        sqLiteDatabase.close();
    }
}
