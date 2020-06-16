package info.devram.dailyexpenses.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.Repository.ExpenseRepository;
import info.devram.dailyexpenses.Repository.IncomeRepository;

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


}
