package info.devram.dailyexpenses.Repository;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.devram.dailyexpenses.Controllers.DatabaseHandler;
import info.devram.dailyexpenses.Core.MyApp;
import info.devram.dailyexpenses.Models.Income;

public class IncomeRepository implements DatabaseService<Income>  {

    private DatabaseHandler db;
    private static IncomeRepository mInstance = null;

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
        return null;

    }

    @Override
    public List<Income> getAll() {

        List<Income> incomeList = new ArrayList<>();

        for (int i = 0 ; i < 3 ; i++) {
            Income income = new Income();
            income.setId(1);
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
