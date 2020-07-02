package info.devram.dainikhatabook.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Repository.ExpenseRepository;
import info.devram.dainikhatabook.Repository.IncomeRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Income>> mutableIncomeLiveData;
    private MutableLiveData<List<Expense>> mutableExpenseLiveData;
    private IncomeRepository incomeRepository;
    private ExpenseRepository expenseRepository;
    private Application application;

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

        List<Income> incomeList = incomeRepository.getAll();
        List<Expense> expenseList = expenseRepository.getAll();

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

    public Boolean addExpense(Expense expense) {
        if (expenseRepository.addData(expense)) {
            return true;
        }
        return false;
    }

    public Boolean addIncome(Income income) {
        if (incomeRepository.addData(income)) {
            return true;
        }
        return false;
    }
}