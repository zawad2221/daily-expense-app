package info.devram.dainikhatabook.Helpers;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Entities.AccountEntity;


public class Util {

    //private static final String TAG = "Util";

    public static List<String> getSum(List<AccountEntity> accountEntities) {

        List<String> totalSum = new ArrayList<>();
        int expenseTotalSum = 0;
        int incomeSum = 0;


        for (int i = 0; i < accountEntities.size(); i++) {
            if (accountEntities.get(i).accountRepoType
                    .getRepoType().equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                expenseTotalSum += accountEntities.get(i).accountMoney.getAmount();
            } else {
                incomeSum += accountEntities.get(i).accountMoney.getAmount();
            }

        }
        totalSum.add(0,String.valueOf(expenseTotalSum));
        totalSum.add(1,String.valueOf(incomeSum));

        return totalSum;
    }
    
    public static List<String> getExpenseTypes()
    {
        final List<String> expenseTypes = new ArrayList<>();
        
        expenseTypes.add("Clothing");
        expenseTypes.add("Entertainment");
        expenseTypes.add("Food");
        expenseTypes.add("Transport");
        expenseTypes.add("Medical");
        expenseTypes.add("Shopping");
        expenseTypes.add("Education");
        expenseTypes.add("Grocery");
        expenseTypes.add("Personal");
        expenseTypes.add("Fuel");

        return expenseTypes;
    }
    
    public static List<String> getIncomeTypes()
    {
        final List<String> incomeTypes = new ArrayList<>();
        
        incomeTypes.add("Cash");
        incomeTypes.add("Salary");
        incomeTypes.add("Loan");

        return incomeTypes;
    }
}
