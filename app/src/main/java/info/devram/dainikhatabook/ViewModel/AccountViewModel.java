package info.devram.dainikhatabook.ViewModel;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Mappers.AccountMapper;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Mappers.ExpenseMapper;
import info.devram.dainikhatabook.Mappers.IncomeMapper;

public class AccountViewModel {

    private static final String TAG = "MainActivityViewModel";

    private AccountMapper accountMapper;
    private Context mContext;
    private List<AccountEntity> accountEntities;

    public AccountViewModel(Context context) {
        this.mContext = context;
    }


    public void init()
    {
        accountMapper = AccountMapper.getInstance(this.mContext);

        this.accountEntities = new ArrayList<>();
        this.accountEntities = this.accountMapper.getAll(null);
        Log.d(TAG, "init: " + this.accountEntities);
    }

    public List<AccountEntity> getAccounts(String type)
    {
        return this.accountEntities;
    }

    public void addAccount(AccountEntity accountEntity)
    {
        this.accountEntities.add(accountEntity);
    }

    public List<AccountEntity> getAccountByTypes(List<String> types)
    {
        return this.accountEntities.stream()
                .filter(accountEntity -> types.contains(accountEntity.accountType.getType()))
                .collect(Collectors.toList());
    }

    public void editAccount(AccountEntity account, int position)
    {
        this.accountEntities.set(position, account);
    }

    public List<AccountEntity> getAccountByType(String type)
    {
        return this.accountEntities.stream()
                .filter(accountEntity -> accountEntity.accountType.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

//    public List<Expense> getExpenseSummaryType(List<String> expenseTypes) {
//        expenseTypes.remove(0);
//        return expenseMapper.getSummaryByType(expenseTypes);
//    }
//
//    public List<Income> getIncomeSummaryType(List<String> incomeTypes) {
//        incomeTypes.remove(0);
//        List<Income> summaryIncomeList = new ArrayList<>();
//
//        for (int i = 0; i < incomeTypes.size(); i++) {
//            int sum = 0;
//            Income newIncome = new Income();
//            for (Income income: getIncomes()) {
//
//                if (income.getIncomeType().equalsIgnoreCase(incomeTypes.get(i))) {
//                    sum += income.getIncomeAmount();
//                    newIncome.setIncomeType(income.getIncomeType());
//                }
//            }
//            if (newIncome.getIncomeType() != null) {
//                newIncome.setIncomeAmount(sum);
//                summaryIncomeList.add(newIncome);
//            }
//        }
//        return summaryIncomeList;
//    }
//
//
//    public void addExpense(Expense expense) {
//
//        String uniqueID = UUID.randomUUID().toString();
//
//        expense.setId(uniqueID);
//        if (expenseList == null) {
//            expenseList = expenseMapper.getAll();
//        }
//        expenseList.add(expense);
//        expenseMapper.addData(expense);
//    }
//
//    public void addIncome(Income income) {
//
//        String uniqueID = UUID.randomUUID().toString();
//
//        income.setId(uniqueID);
//        if (incomeList == null) {
//            incomeList = incomeMapper.getAll();
//        }
//
//        incomeList.add(income);
//        incomeMapper.addData(income);
//    }
//
//    public Boolean editExpense(int position,Expense editedExpense) {
//
//        if (expenseMapper.onUpdate(editedExpense)) {
//            expenseList.set(position,editedExpense);
//            return true;
//        }
//        return false;
//    }
//    public Boolean editIncome(int position,Income editedIncome) {
//
//        if (incomeMapper.onUpdate(editedIncome)) {
//            incomeList.set(position,editedIncome);
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean deleteExpense(int position) {
//        Expense expense = expenseList.get(position);
//        if (expenseMapper.onDelete(expense)) {
//            expenseList.remove(position);
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean deleteIncome(int position) {
//        Income income = incomeList.get(position);
//        if (incomeMapper.onDelete(income)) {
//            incomeList.remove(position);
//            return true;
//        }
//        return false;
//    }
//
//    public List<Expense> getExpenseSyncList() {
//
//        List<Expense> isNotSynced = new ArrayList<>();
//
//        for (int i = 0; i < expenseList.size(); i++) {
//            if (!expenseList.get(i).getSyncStatus()) {
//                isNotSynced.add(expenseList.get(i));
//            }
//        }
//        return isNotSynced;
//    }
//
//    public List<Income> getIncomeSyncList() {
//
//        List<Income> isNotSynced = new ArrayList<>();
//
//        for (int i = 0; i < incomeList.size(); i++) {
//            if (!incomeList.get(i).getSyncStatus()) {
//                isNotSynced.add(incomeList.get(i));
//            }
//        }
//        return isNotSynced;
//    }
//
//    public void updateSyncListWithDb(List<DashBoardObject> syncList) {
//        List<Expense> expenseSyncedList = new ArrayList<>();
//        List<Income> incomeSyncedList = new ArrayList<>();
//        for (int i = 0; i < syncList.size(); i++) {
//            if(syncList.get(i).getIsExpense()) {
//                Expense expense = new Expense();
//                expense.setExpenseType(syncList.get(i).getTypeObject());
//                expense.setExpenseAmount(syncList.get(i).getAmountObject());
//                expense.setId(syncList.get(i).getIdObject());
//                expense.setExpenseDate(syncList.get(i).getDateObject());
//                expense.setExpenseDesc(syncList.get(i).getDescObject());
//                expense.setSyncStatus(true);
//                expenseSyncedList.add(expense);
//            }else {
//                Income income = new Income();
//                income.setIncomeType(syncList.get(i).getTypeObject());
//                income.setIncomeAmount(syncList.get(i).getAmountObject());
//                income.setId(syncList.get(i).getIdObject());
//                income.setIncomeDate(syncList.get(i).getDateObject());
//                income.setIncomeDesc(syncList.get(i).getDescObject());
//                income.setSyncStatus(true);
//                incomeSyncedList.add(income);
//            }
//        }
//        if (expenseSyncedList.size() > 0) {
//            expenseMapper.updateSync(expenseSyncedList);
//        }
//        if (incomeSyncedList.size() > 0) {
//            incomeMapper.updateSync(incomeSyncedList);
//        }
//    }
//
//    public List<Expense> getExpenseByType(String type) {
//        return expenseMapper.getByType(type);
//    }
//
//    public List<Income> getIncomeByType(String type) {
//        List<Income> typeIncomeList = new ArrayList<>();
//        for (Income income: this.incomeList) {
//            if (income.getIncomeType().equalsIgnoreCase(type)) {
//                typeIncomeList.add(income);
//
//            }
//        }
//        return typeIncomeList;
//    }
//
//    public List<DashBoardObject> getDataForSyncing() {
//        List<Expense> expenseList = this.getExpenseSyncList();
//        List<Income> incomeList = this.getIncomeSyncList();
//        List<DashBoardObject> dashBoardObjects = new ArrayList<>();
//
//        for (Expense expense: expenseList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setIdObject(expense.getId());
//            dashBoardObject.setTypeObject(expense.getExpenseType());
//            dashBoardObject.setDateObject(expense.getExpenseDate());
//            dashBoardObject.setDescObject(expense.getExpenseDesc());
//            dashBoardObject.setAmountObject(expense.getExpenseAmount());
//            dashBoardObject.setSyncStatus(expense.getSyncStatus());
//            dashBoardObject.setIsExpense(true);
//            dashBoardObjects.add(dashBoardObject);
//        }
//        for (Income income: incomeList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setIdObject(income.getId());
//            dashBoardObject.setTypeObject(income.getIncomeType());
//            dashBoardObject.setDateObject(income.getIncomeDate());
//            dashBoardObject.setDescObject(income.getIncomeDesc());
//            dashBoardObject.setAmountObject(income.getIncomeAmount());
//            dashBoardObject.setSyncStatus(income.getSyncStatus());
//            dashBoardObject.setIsExpense(false);
//            dashBoardObjects.add(dashBoardObject);
//        }
//
//
//        return dashBoardObjects;
//    }
//
//    public List<AccountEntity> getDataForReport() {
//        return this.
//        List<Expense> expenseList = this.getExpenses();
//        List<Income> incomeList = this.getIncomes();
//        List<DashBoardObject> dashBoardObjects = new ArrayList<>();
//        for (Expense expense: expenseList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setIdObject(expense.getId());
//            dashBoardObject.setTypeObject(expense.getExpenseType());
//            dashBoardObject.setDateObject(expense.getExpenseDate());
//            dashBoardObject.setDescObject(expense.getExpenseDesc());
//            dashBoardObject.setAmountObject(expense.getExpenseAmount());
//            dashBoardObject.setSyncStatus(expense.getSyncStatus());
//            dashBoardObject.setIsExpense(true);
//            dashBoardObjects.add(dashBoardObject);
//        }
//        for (Income income: incomeList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setIdObject(income.getId());
//            dashBoardObject.setTypeObject(income.getIncomeType());
//            dashBoardObject.setDateObject(income.getIncomeDate());
//            dashBoardObject.setDescObject(income.getIncomeDesc());
//            dashBoardObject.setAmountObject(income.getIncomeAmount());
//            dashBoardObject.setSyncStatus(income.getSyncStatus());
//            dashBoardObject.setIsExpense(false);
//            dashBoardObjects.add(dashBoardObject);
//        }
//        return dashBoardObjects;
//    }
}
