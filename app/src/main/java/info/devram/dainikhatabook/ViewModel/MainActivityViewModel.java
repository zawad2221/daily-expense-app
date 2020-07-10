package info.devram.dainikhatabook.ViewModel;

import android.app.Application;
import android.content.Context;
//import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Hashtable;
import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Repository.ExpenseRepository;
import info.devram.dainikhatabook.Repository.IncomeRepository;

public class MainActivityViewModel extends AndroidViewModel {

    //private static final String TAG = "MainActivityViewModel";

    private MutableLiveData<List<Income>> mutableIncomeLiveData;
    private MutableLiveData<List<Expense>> mutableExpenseLiveData;
    private IncomeRepository incomeRepository;
    private ExpenseRepository expenseRepository;
    private Application application;
    private List<Expense> expenseList;
    private List<Income> incomeList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }


    public void init() {
        if (mutableIncomeLiveData != null && mutableExpenseLiveData != null) {
            return;
        }

        Context context = this.application.getApplicationContext();
        incomeRepository = IncomeRepository
                .getInstance(context);

        expenseRepository = ExpenseRepository
                .getInstance(context);

        incomeList = incomeRepository.getAll();
        expenseList = expenseRepository.getAll();

        mutableIncomeLiveData = new MutableLiveData<>();
        mutableExpenseLiveData = new MutableLiveData<>();

        mutableIncomeLiveData.setValue(incomeList);
        mutableExpenseLiveData.setValue(expenseList);
    }

    public LiveData<List<Income>> getIncomes() {
        return mutableIncomeLiveData;
    }

    public LiveData<List<Expense>> getExpenses() {
        return mutableExpenseLiveData;
    }

    public void addExpense(Hashtable<String,String> hashtable) {
        Expense expense = new Expense();
        expense.setExpenseType(hashtable.get("type"));
        expense.setExpenseDate(hashtable.get("date"));
        expense.setExpenseAmount(Integer.parseInt(hashtable.get("amount")));
        expense.setExpenseDesc(hashtable.get("desc"));

        int id = expenseRepository.addData(expense);

        if (id != -1) {
            expense = expenseRepository.getOne(id);
            expenseList.add(expense);
            mutableExpenseLiveData.postValue(expenseList);
        }

    }

    public void addIncome(Hashtable<String,String> hashtable) {
        Income income = new Income();
        income.setIncomeType(hashtable.get("type"));
        income.setIncomeDate(hashtable.get("date"));
        income.setIncomeAmount(Integer.parseInt(hashtable.get("amount")));
        income.setIncomeDesc(hashtable.get("desc"));
        int id = incomeRepository.addData(income);

        if (id != -1) {
            income = incomeRepository.getOne(id);
            incomeList.add(income);
            mutableIncomeLiveData.postValue(incomeList);
        }
    }

    public Boolean editExpense(int position,Hashtable<String,String> hashtable) {
        Expense expense = mutableExpenseLiveData.getValue().get(position);
        expense.setExpenseType(hashtable.get("type"));
        expense.setExpenseDate(hashtable.get("date"));
        expense.setExpenseAmount(Integer.parseInt(hashtable.get("amount")));
        expense.setExpenseDesc(hashtable.get("desc"));
        if (expenseRepository.onUpdate(expense)) {
            expenseList.set(position,expense);
            mutableExpenseLiveData.postValue(expenseList);
            return true;
        }
        return false;
    }
    public Boolean editIncome(int position,Hashtable<String,String> hashtable) {
        Income income = mutableIncomeLiveData.getValue().get(position);
        income.setIncomeType(hashtable.get("type"));
        income.setIncomeDate(hashtable.get("date"));
        income.setIncomeAmount(Integer.parseInt(hashtable.get("amount")));
        income.setIncomeDesc(hashtable.get("desc"));
        if (incomeRepository.onUpdate(income)) {
            incomeList.set(position,income);
            mutableIncomeLiveData.postValue(incomeList);
            return true;
        }
        return false;
    }

    public Boolean deleteExpense(int position) {
        Expense expense = mutableExpenseLiveData.getValue().get(position);
        if (expenseRepository.onDelete(expense)) {
            expenseList.remove(position);
            mutableExpenseLiveData.postValue(expenseList);
            return true;
        }
        return false;
    }

    public Boolean deleteIncome(int position) {
        Income income = mutableIncomeLiveData.getValue().get(position);
        if (incomeRepository.onDelete(income)) {
            incomeList.remove(position);
            mutableIncomeLiveData.postValue(incomeList);
            return true;
        }
        return false;
    }
}
