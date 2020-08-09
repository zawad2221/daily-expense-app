package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class AddActivity extends BaseAddActivity {

    //private static final String TAG = "ExpenseActivity";

    private boolean hasExpense = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        activateToolbar(true);

        hasExpense = getIntent().hasExtra(Expense.class.getSimpleName());

        if (hasExpense) {
            setTitle("Add Expense");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(this,
                            R.array.expense_type, android.R.layout.simple_spinner_item);
            setupSpinner(adapter);
        }else {
            setTitle("Add Income");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(this,
                            R.array.income_type, android.R.layout.simple_spinner_item);
            setupSpinner(adapter);
        }

        Button addNew = findViewById(R.id.add_new_btn);

        setupBase();

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseData();
                addData();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addData() {
        Intent resultIntent = getIntent();
        if (hasExpense) {
            Expense expense = new Expense();
            expense.setExpenseType(selectedType);
            expense.setExpenseAmount(selectedAmount);
            expense.setExpenseDate(parsedDate);
            expense.setExpenseDesc(selectedDesc);
            resultIntent.putExtra(Expense.class.getSimpleName(),expense);
        }else {
            Income income = new Income();
            income.setIncomeType(selectedType);
            income.setIncomeAmount(selectedAmount);
            income.setIncomeDate(parsedDate);
            income.setIncomeDesc(selectedDesc);
            resultIntent.putExtra(Income.class.getSimpleName(),income);
        }
        setResult(1,resultIntent);
    }
}