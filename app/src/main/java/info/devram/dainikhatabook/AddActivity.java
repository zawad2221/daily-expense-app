package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Values.AccountCreatedDate;
import info.devram.dainikhatabook.Values.AccountDescription;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountSyncStatus;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;
import info.devram.dainikhatabook.databinding.AddActivityBinding;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class AddActivity extends BaseAddActivity implements View.OnClickListener {

    //private static final String TAG = "ExpenseActivity";

    private boolean hasExpense = false;
    private AddActivityBinding binding =  null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activateToolbar(true);

        binding.includeAddForm.addNewBtn.setOnClickListener(this);

        hasExpense = getIntent().hasExtra(Config.EXPENSE_TABLE_NAME);

        List<String> items;

        if (hasExpense) {
            setTitle("Add Expense");

            items = Util.getExpenseTypes();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,items);


            items.add(0, getString(R.string.select_type));

            setupSpinner(adapter);
        }else {
            setTitle("Add Income");

            items = Util.getIncomeTypes();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,items);

            items.add(0, getString(R.string.select_type));

            setupSpinner(adapter);
        }

//        Button addNew = findViewById(R.id.add_new_btn);
//
        setupBase();
//
//        addNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parseData();
//                addData();
//                finish();
//            }
//        });

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
        String uniqueID = UUID.randomUUID().toString();
        AccountID accountID = new AccountID(uniqueID);
        AccountType accountType = new AccountType(selectedType);
        Money money = new Money(selectedAmount);
        AccountCreatedDate accountCreatedDate = new AccountCreatedDate(parsedDate);
        AccountDescription accountDescription = new AccountDescription(selectedDesc);
        AccountSyncStatus accountSyncStatus = new AccountSyncStatus();
        AccountEntity accountEntity = new AccountEntity(
                accountID,
                accountType,
                accountCreatedDate,
                money,
                accountDescription,
                accountSyncStatus
        );

        if (hasExpense) {
            resultIntent.putExtra(Config.EXPENSE_TABLE_NAME,accountEntity);
        }else {
            resultIntent.putExtra(Config.INCOME_TABLE_NAME,accountEntity);
        }
        setResult(1,resultIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_new_btn:
                if (binding.includeAddForm.addNewSpinner.getSelectedItem().toString() == getString(R.string.select_type)) {
                    ((TextView) binding.includeAddForm.addNewSpinner.getSelectedView()).setError(getString(
                            R.string.select_type_error
                    ));
                    return;
                }
                if(binding.includeAddForm.editTextAmount.getText().toString().equals("") ||
                        binding.includeAddForm.editTextAmount.getText().toString().equals("0")){
                    binding.includeAddForm.editTextAmount.setError("Can't be empty or zero");
                    return;
                }
                parseData();
                addData();
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}