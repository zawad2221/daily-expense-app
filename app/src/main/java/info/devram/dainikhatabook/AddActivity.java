package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.UUID;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Values.AccountCreatedDate;
import info.devram.dainikhatabook.Values.AccountDescription;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountSyncStatus;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class AddActivity extends BaseAddActivity {

    private static final String TAG = "ExpenseActivity";

    private boolean hasExpense = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        activateToolbar(true);

        hasExpense = getIntent().hasExtra(Config.EXPENSE_TABLE_NAME);

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
        Log.d(TAG, "addData: " + accountEntity);
        if (hasExpense) {
            resultIntent.putExtra(Config.EXPENSE_TABLE_NAME,accountEntity);
        }else {
            resultIntent.putExtra(Config.INCOME_TABLE_NAME,accountEntity);
        }
        setResult(1,resultIntent);
    }
}