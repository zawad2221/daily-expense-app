package info.devram.dainikhatabook.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Repository.ExpenseRepository;
import info.devram.dainikhatabook.Repository.IncomeRepository;

public class MainActivityViewModel {

    private static final String TAG = "MainActivityViewModel";

    private IncomeRepository incomeRepository;
    private ExpenseRepository expenseRepository;
    private Context mContext;
    private List<Expense> expenseList = null;
    private List<Income> incomeList;

    public MainActivityViewModel(Context context) {
        this.mContext = context;
    }


    public void init() {

        incomeRepository = IncomeRepository
                .getInstance(mContext);

        expenseRepository = ExpenseRepository
                .getInstance(mContext);

        incomeList = incomeRepository.getAll();
        expenseList = expenseRepository.getAll();

    }

    public List<Income> getIncomes() {
        return incomeList;
    }

    public List<Expense> getExpenses() {
        return expenseList;
    }

    public void addExpense(Expense expense) {

        String uniqueID = UUID.randomUUID().toString();

        expense.setId(uniqueID);
        expenseList.add(expense);

        expenseRepository.addData(expense);
    }

    public void addIncome(Income income) {

        String uniqueID = UUID.randomUUID().toString();

        income.setId(uniqueID);

        incomeList.add(income);

        incomeRepository.addData(income);

    }

    public Boolean editExpense(int position,Expense editedExpense) {

        if (expenseRepository.onUpdate(editedExpense)) {
            expenseList.set(position,editedExpense);
            return true;
        }
        return false;
    }
    public Boolean editIncome(int position,Income editedIncome) {
        Income income = incomeList.get(position);

        if (incomeRepository.onUpdate(income)) {
            incomeList.set(position,income);
            return true;
        }
        return false;
    }

    public Boolean deleteExpense(int position) {
        Expense expense = expenseList.get(position);
        if (expenseRepository.onDelete(expense)) {
            expenseList.remove(position);
            return true;
        }
        return false;
    }

    public Boolean deleteIncome(int position) {
        Income income = incomeList.get(position);
        if (incomeRepository.onDelete(income)) {
            incomeList.remove(position);
            return true;
        }
        return false;
    }

    public List<Expense> getExpenseSyncList() {

        List<Expense> isNotSynced = new ArrayList<>();

        for (int i = 0; i < expenseList.size(); i++) {
            if (!expenseList.get(i).getSyncStatus()) {
                isNotSynced.add(expenseList.get(i));
            }
        }
        return isNotSynced;
    }

    public List<Income> getIncomeSyncList() {

        List<Income> isNotSynced = new ArrayList<>();

        for (int i = 0; i < incomeList.size(); i++) {
            if (!incomeList.get(i).getSyncStatus()) {
                isNotSynced.add(incomeList.get(i));
            }
        }
        return isNotSynced;
    }

    public void updateExpenseSyncListWithDb(List<Expense> syncList) {
        for (int i = 0; i < syncList.size(); i++) {
            syncList.get(i).setSyncStatus(true);
        }
        expenseRepository.updateSync(syncList);
    }

    public void updateIncomeSyncListWithDb(List<Income> syncList) {
        for (int i = 0; i < syncList.size(); i++) {
            syncList.get(i).setSyncStatus(true);
        }
        incomeRepository.updateSync(syncList);
    }
}
