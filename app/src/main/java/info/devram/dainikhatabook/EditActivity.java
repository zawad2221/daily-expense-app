package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.util.Date;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class EditActivity extends BaseAddActivity {

    private static final String TAG = "EditActivity";

    private boolean hasExpense = false;
    private Expense expense;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        activateToolbar(true);

        hasExpense = getIntent().hasExtra(Expense.class.getSimpleName());
        if (hasExpense) {
            setTitle("Edit Expense");
            adapter = ArrayAdapter
                    .createFromResource(this,
                            R.array.expense_type, android.R.layout.simple_spinner_item);
            setupSpinner(adapter);
            expense = (Expense) getIntent()
                    .getSerializableExtra(Expense.class.getSimpleName());
            Log.d(TAG, "onCreate: " + expense);
        }


        setupBase();
        setupUIForEdit();

        Button editButton = findViewById(R.id.add_new_btn);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseData();
                editData();
                finish();
            }
        });

    }

    private void setupUIForEdit() {
        int spinnerPosition = adapter.getPosition(expense.getExpenseType());
        spinner.setSelection(spinnerPosition);
        try {
            String selectedDate = sdf.format(expense.getExpenseDate());
            datePicker.setText(selectedDate);
        } catch (NumberFormatException e) {
            Log.e(TAG, "onClick parsing string to int " + e.getMessage());
        }
        amountEditText.setText(String.valueOf(expense.getExpenseAmount()));
        descEditText.setText(expense.getExpenseDesc());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editData() {
        Intent resultIntent = getIntent();
        if (hasExpense) {
            expense.setExpenseType(selectedType);
            expense.setExpenseAmount(selectedAmount);
            expense.setExpenseDate(parsedDate);
            expense.setExpenseDesc(selectedDesc);
            resultIntent.putExtra(Expense.class.getSimpleName(),expense);
            setResult(1,resultIntent);
        }else {
            Income income = new Income();
            income.setIncomeType(selectedType);
            income.setIncomeAmount(selectedAmount);
            income.setIncomeDate(parsedDate);
            income.setIncomeDesc(selectedDesc);
            resultIntent.putExtra(Income.class.getSimpleName(),income);
            setResult(1,resultIntent);
        }
    }
}
