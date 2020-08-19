package info.devram.dainikhatabook.ViewModel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import info.devram.dainikhatabook.Models.DashBoardObject;
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
    private List<Income> incomeList = null;

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
        if (incomeList != null) {
            return incomeList;
        }
        incomeList = incomeRepository.getAll();
        return incomeList;
    }

    public List<Expense> getExpenses() {
        if (expenseList != null) {
            return expenseList;
        }
        expenseList = expenseRepository.getAll();
        return expenseList;
    }

    public void setExpenses() {
        expenseList = null;
    }

    public void setIncomes() {
        incomeList = null;
    }

    public List<Expense> getExpenseSummaryType(List<String> expenseTypes) {
        expenseTypes.remove(0);
        return expenseRepository.getSummaryByType(expenseTypes);
    }

    public List<Income> getIncomeSummaryType(List<String> incomeTypes) {
        incomeTypes.remove(0);
        List<Income> summaryIncomeList = new ArrayList<>();

        for (int i = 0; i < incomeTypes.size(); i++) {
            int sum = 0;
            Income newIncome = new Income();
            for (Income income: getIncomes()) {

                if (income.getIncomeType().equalsIgnoreCase(incomeTypes.get(i))) {
                    sum += income.getIncomeAmount();
                    newIncome.setIncomeType(income.getIncomeType());
                }
            }
            if (newIncome.getIncomeType() != null) {
                newIncome.setIncomeAmount(sum);
                summaryIncomeList.add(newIncome);
            }
        }
        return summaryIncomeList;
    }


    public void addExpense(Expense expense) {

        String uniqueID = UUID.randomUUID().toString();

        expense.setId(uniqueID);
        if (expenseList == null) {
            expenseList = expenseRepository.getAll();
        }
        expenseList.add(expense);
        expenseRepository.addData(expense);
    }

    public void addIncome(Income income) {

        String uniqueID = UUID.randomUUID().toString();

        income.setId(uniqueID);
        if (incomeList == null) {
            incomeList = incomeRepository.getAll();
        }

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

        if (incomeRepository.onUpdate(editedIncome)) {
            incomeList.set(position,editedIncome);
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

    public void updateSyncListWithDb(List<DashBoardObject> syncList) {
        List<Expense> expenseSyncedList = new ArrayList<>();
        List<Income> incomeSyncedList = new ArrayList<>();
        for (int i = 0; i < syncList.size(); i++) {
            if(syncList.get(i).getIsExpense()) {
                Expense expense = new Expense();
                expense.setExpenseType(syncList.get(i).getTypeObject());
                expense.setExpenseAmount(syncList.get(i).getAmountObject());
                expense.setId(syncList.get(i).getIdObject());
                expense.setExpenseDate(syncList.get(i).getDateObject());
                expense.setExpenseDesc(syncList.get(i).getDescObject());
                expense.setSyncStatus(true);
                expenseSyncedList.add(expense);
            }else {
                Income income = new Income();
                income.setIncomeType(syncList.get(i).getTypeObject());
                income.setIncomeAmount(syncList.get(i).getAmountObject());
                income.setId(syncList.get(i).getIdObject());
                income.setIncomeDate(syncList.get(i).getDateObject());
                income.setIncomeDesc(syncList.get(i).getDescObject());
                income.setSyncStatus(true);
                incomeSyncedList.add(income);
            }
        }
        if (expenseSyncedList.size() > 0) {
            expenseRepository.updateSync(expenseSyncedList);
        }
        if (incomeSyncedList.size() > 0) {
            incomeRepository.updateSync(incomeSyncedList);
        }
    }

    public List<Expense> getExpenseByType(String type) {
        return expenseRepository.getByType(type);
    }

    public List<Income> getIncomeByType(String type) {
        List<Income> typeIncomeList = new ArrayList<>();
        for (Income income: this.incomeList) {
            if (income.getIncomeType().equalsIgnoreCase(type)) {
                typeIncomeList.add(income);

            }
        }
        return typeIncomeList;
    }

    public List<DashBoardObject> getDataForSyncing() {
        List<Expense> expenseList = this.getExpenseSyncList();
        List<Income> incomeList = this.getIncomeSyncList();
        List<DashBoardObject> dashBoardObjects = new ArrayList<>();

        for (Expense expense: expenseList) {
            DashBoardObject dashBoardObject = new DashBoardObject();
            dashBoardObject.setIdObject(expense.getId());
            dashBoardObject.setTypeObject(expense.getExpenseType());
            dashBoardObject.setDateObject(expense.getExpenseDate());
            dashBoardObject.setDescObject(expense.getExpenseDesc());
            dashBoardObject.setAmountObject(expense.getExpenseAmount());
            dashBoardObject.setSyncStatus(expense.getSyncStatus());
            dashBoardObject.setIsExpense(true);
            dashBoardObjects.add(dashBoardObject);
        }
        for (Income income: incomeList) {
            DashBoardObject dashBoardObject = new DashBoardObject();
            dashBoardObject.setIdObject(income.getId());
            dashBoardObject.setTypeObject(income.getIncomeType());
            dashBoardObject.setDateObject(income.getIncomeDate());
            dashBoardObject.setDescObject(income.getIncomeDesc());
            dashBoardObject.setAmountObject(income.getIncomeAmount());
            dashBoardObject.setSyncStatus(income.getSyncStatus());
            dashBoardObject.setIsExpense(false);
            dashBoardObjects.add(dashBoardObject);
        }


        return dashBoardObjects;
    }
}
