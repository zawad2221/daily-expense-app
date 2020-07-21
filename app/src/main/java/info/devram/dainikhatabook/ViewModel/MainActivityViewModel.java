package info.devram.dainikhatabook.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

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

    public void addExpense(Intent intentData) {

        Expense expense = getExpenseIntentData(intentData);

        expenseList.add(expense);

        expenseRepository.addData(expense);
    }

    public void addIncome(Intent intentData) {
        Income income = getIncomeIntentData(intentData);

        int id = incomeRepository.addData(income);

        if (id != -1) {
            income = incomeRepository.getOne(id);
            incomeList.add(income);
        }
    }

    public Boolean editExpense(int position,Intent data) {

        Expense expense = expenseList.get(position);

        expense.setExpenseType(data.getStringExtra("type").toLowerCase());
        expense.setExpenseDate(data.getStringExtra("date"));
        expense.setExpenseAmount(data.getIntExtra("amount", 0));
        expense.setExpenseDesc(data.getStringExtra("desc"));

        if (expenseRepository.onUpdate(expense)) {
            expenseList.set(position,expense);
            return true;
        }
        return false;
    }
    public Boolean editIncome(int position,Intent data) {
        Income income = incomeList.get(position);
        income.setIncomeType(data.getStringExtra("type").toLowerCase());
        income.setIncomeDate(data.getStringExtra("date"));
        income.setIncomeAmount(data.getIntExtra("amount", 0));
        income.setIncomeDesc(data.getStringExtra("desc"));
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

    public Expense getExpenseIntentData(Intent data) {
        Expense expense = new Expense();

        expense.setExpenseType(data.getStringExtra("type").toLowerCase());
        expense.setExpenseDate(data.getStringExtra("date"));
        expense.setExpenseAmount(data.getIntExtra("amount", 0));
        expense.setExpenseDesc(data.getStringExtra("desc"));

        return expense;

    }

    public Income getIncomeIntentData(Intent data) {
        Income income = new Income();

        income.setIncomeType(data.getStringExtra("type").toLowerCase());
        income.setIncomeDate(data.getStringExtra("date"));
        income.setIncomeAmount(data.getIntExtra("amount", 0));
        income.setIncomeDesc(data.getStringExtra("desc"));

        return income;

    }
}
