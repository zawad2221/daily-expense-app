package info.devram.dainikhatabook.Repository;

import android.content.Context;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Models.Income;

public class IncomeRepository implements DatabaseService<Income>  {

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
    public Boolean addData(Income obj) {
        try{

            incomeList.add(obj);
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
    public List<Income> getAll() {

        incomeList = new ArrayList<>();

        for (int i = 0 ; i < 5 ; i++) {
            Income income = new Income();
            income.setId(i);
            income.setIncomeType("cash");
            income.setIncomeAmount(10000);
            incomeList.add(income);
        }


        return incomeList;
    }

    @Override
    public Income getOne() {
        return null;
    }

    @Override
    public int onUpdate(Income obj) {
        return 0;
    }

    @Override
    public void onDelete(Income obj) {

    }

    @Override
    public int getCount() {
        return 0;
    }
}
