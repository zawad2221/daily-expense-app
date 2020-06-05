package info.devram.dailyexpenses.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.Repository.IncomeRepository;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Income>> mutableIncomeLiveData;
    private MutableLiveData<List<Expense>> mutableExpenseLiveData;
    private IncomeRepository incomeRepository;

    public void init() {
        if (mutableIncomeLiveData != null && mutableExpenseLiveData != null) {
            return;
        }
        incomeRepository = new IncomeRepository();
        mutableIncomeLiveData = incomeRepository.getAll();
    }

    public LiveData<List<Income>> getIncomes() {
        return mutableIncomeLiveData;
    }

    public LiveData<List<Expense>> getExpenses() {
        return mutableExpenseLiveData;
    }
}
