package info.devram.dailyexpenses.Repository;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import info.devram.dailyexpenses.Controllers.DatabaseHandler;
import info.devram.dailyexpenses.Models.Income;

public class IncomeRepository  {

    private List<Income> incomeList = new ArrayList<>();

    public IncomeRepository() {
    }

    public void addData(DatabaseHandler db) {

    }


    public MutableLiveData<List<Income>> getAll(/*DatabaseHandler db*/) {



        for (int i = 0 ; i < 3 ; i++) {
            Income income = new Income();
            income.setId(1);
            income.setIncomeType("cash");
            income.setIncomeAmount(10000);
            incomeList.add(income);
        }


        MutableLiveData<List<Income>> mIncomeList = new MutableLiveData<>();

        mIncomeList.setValue(incomeList);

        return mIncomeList;
    }


    public Income getOne(DatabaseHandler db) {
        return null;
    }


    public void update(DatabaseHandler db, Object data) {

    }


    public void delete(DatabaseHandler db, Object data) {

    }
}
