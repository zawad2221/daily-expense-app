package info.devram.dainikhatabook.Repository;

import android.content.Context;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Models.Expense;

public class ExpenseRepository implements DatabaseService<Expense> {

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
    public Boolean addData(Expense obj) {
        try{

            expenseList.add(obj);
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(Util.EXPENSE_KEY_TYPE,obj.getExpenseType());
//            contentValues.put(Util.EXPENSE_KEY_DESC,obj.getExpenseDesc());
//            contentValues.put(Util.EXPENSE_KEY_AMOUNT,obj.getExpenseAmount());
//            contentValues.put(Util.EXPENSE_KEY_DATE,obj.getExpenseDate());
//
//            db.getWritableDatabase().insert(Util.EXPENSE_TABLE_NAME,
//                    null,contentValues);

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<Expense> getAll() {

        expenseList = new ArrayList<>();

        for (int i = 0 ; i < 5 ; i++) {
            Expense expense = new Expense();
            expense.setId(1);
            expense.setExpenseType("clothing");
            expense.setExpenseAmount(10000);
            expenseList.add(expense);
        }

//        String query = "SELECT * FROM " + Util.EXPENSE_TABLE_NAME;
//
//        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);
//
//        if(cursor.moveToFirst()) {
//            do {
//
//                Expense expense = new Expense();
//                expense.setId(cursor
//                        .getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_ID)));
//                expense.setExpenseType(cursor
//                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_TYPE)));
//                expense.setExpenseDesc(cursor
//                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DESC)));
//                expense.setExpenseAmount(cursor
//                        .getInt(cursor.getColumnIndex(Util.EXPENSE_KEY_AMOUNT)));
//                expense.setExpenseDate(cursor
//                        .getString(cursor.getColumnIndex(Util.EXPENSE_KEY_DATE)));
//                expenseList.add(expense);
//            }while (cursor.moveToNext());
//        }
//
//        cursor.close();

        return expenseList;
    }

    @Override
    public Expense getOne() {
        return null;
    }

    @Override
    public int onUpdate(Expense obj) {
        return 0;
    }

    @Override
    public void onDelete(Expense obj) {

    }

    @Override
    public int getCount() {
        return 0;
    }
}