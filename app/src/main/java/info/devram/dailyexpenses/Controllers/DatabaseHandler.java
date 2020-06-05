package info.devram.dailyexpenses.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import info.devram.dailyexpenses.Config.Util;
import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.Models.ModelHandler;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static DatabaseHandler mInstance = null;
    private ModelHandler expenseModel = new Expense.Model();

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
    public void onCreate(SQLiteDatabase db) {
        expenseModel.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
