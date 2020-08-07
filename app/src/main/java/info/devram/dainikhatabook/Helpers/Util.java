package info.devram.dainikhatabook.Helpers;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;

public class Util {

    public static List<String> getSum(List<Expense> expenseOBJ, List<Income> incomeOBJ) {

        List<String> totalSum = new ArrayList<>();
        int expenseTotalSum = 0;
        int incomeSum = 0;

        if (expenseOBJ.size() == 0) {
            return totalSum;
        }
        for (int i = 0; i < expenseOBJ.size(); i++) {
            expenseTotalSum += expenseOBJ.get(i).getExpenseAmount();
        }

        for (int i = 0; i < incomeOBJ.size(); i++) {
            incomeSum += incomeOBJ.get(i).getIncomeAmount();

        }

        totalSum.add(0,String.valueOf(expenseTotalSum));
        totalSum.add(1,String.valueOf(incomeSum));

        return totalSum;
    }
}
