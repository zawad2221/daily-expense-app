package info.devram.dainikhatabook.ViewModel;

import android.content.Context;

import java.util.Hashtable;
import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Repository.ExpenseRepository;
import info.devram.dainikhatabook.Repository.IncomeRepository;

public class MainActivityViewModel {

    //private static final String TAG = "MainActivityViewModel";

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

    public void addExpense(Hashtable<String,String> hashtable) {
        Expense expense = new Expense();
        expense.setExpenseType(hashtable.get("type"));
        expense.setExpenseDate(hashtable.get("date"));
        expense.setExpenseAmount(Integer.parseInt(hashtable.get("amount")));
        expense.setExpenseDesc(hashtable.get("desc"));

        expenseList.add(expense);

        expenseRepository.addData(expense);
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
        }
    }

    public Boolean editExpense(int position,Hashtable<String,String> hashtable) {
        Expense expense = expenseList.get(position);
        expense.setExpenseType(hashtable.get("type"));
        expense.setExpenseDate(hashtable.get("date"));
        expense.setExpenseAmount(Integer.parseInt(hashtable.get("amount")));
        expense.setExpenseDesc(hashtable.get("desc"));
        if (expenseRepository.onUpdate(expense)) {
            expenseList.set(position,expense);
            return true;
        }
        return false;
    }
    public Boolean editIncome(int position,Hashtable<String,String> hashtable) {
        Income income = incomeList.get(position);
        income.setIncomeType(hashtable.get("type"));
        income.setIncomeDate(hashtable.get("date"));
        income.setIncomeAmount(Integer.parseInt(hashtable.get("amount")));
        income.setIncomeDesc(hashtable.get("desc"));
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
}
