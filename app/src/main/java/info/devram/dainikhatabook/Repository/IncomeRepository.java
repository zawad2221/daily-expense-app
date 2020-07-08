package info.devram.dainikhatabook.Repository;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
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
    public Boolean addData(Income obj) {
        try{
            obj.setId(incomeList.size() + 1);
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



//        for (int i = 0 ; i < 5 ; i++) {
//            Income income = new Income();
//            income.setId(i + 1);
//            income.setId(i);
//            income.setIncomeType("cash");
//            income.setIncomeDate(getDate());
//            income.setIncomeAmount(10000);
//            income.setIncomeDesc("gauri");
//            incomeList.add(income);
//        }


        return incomeList;
    }

    @Override
    public Income getOne() {
        return null;
    }

    @Override
    public Boolean onUpdate(Income obj) {
        for (int i =0; i < incomeList.size(); i++) {
            if (incomeList.get(i).getId() == obj.getId()) {

                Log.i(TAG, "new obj " + obj);
                incomeList.set(i,obj);
                Log.i(TAG, "list obj " + incomeList.get(i));
                return true;
            }
        }
        return null;
    }

    @Override
    public Boolean onDelete(Income obj) {
        for (int i =0; i < incomeList.size(); i++) {
            if (incomeList.get(i).getId() == obj.getId()) {
                incomeList.remove(i);
                return true;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    private String getDate() {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);

        return sdf.format(myCalendar.getTime());
    }
}
