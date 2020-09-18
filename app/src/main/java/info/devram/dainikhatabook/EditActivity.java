package info.devram.dainikhatabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.LogError;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Interfaces.FileErrorLoggerListener;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.ui.BaseAddActivity;

public class EditActivity extends BaseAddActivity implements FileErrorLoggerListener {

    private static final String TAG = "EditActivity";

    private boolean hasExpense = false;
    private ArrayAdapter<String> adapter;
    private AccountEntity accountEntity;
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        activateToolbar(true);

        hasExpense = getIntent().hasExtra(Config.EXPENSE_TABLE_NAME);

        List<String> items;

        if (hasExpense) {
            setTitle("Add Expense");

            items = Util.getExpenseTypes();


        } else {
            setTitle("Add Income");

            items = Util.getIncomeTypes();

        }
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        items.add(0, "Select Type");
        setupSpinner(adapter);


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

        if (hasExpense) {
            accountEntity = (AccountEntity) getIntent()
                    .getSerializableExtra(Config.EXPENSE_TABLE_NAME);

        } else {

            accountEntity = (AccountEntity) getIntent()
                    .getSerializableExtra(Config.INCOME_TABLE_NAME);

        }

        if (accountEntity != null) {
            int spinnerPosition = adapter.getPosition(accountEntity.accountType.getType());
            spinner.setSelection(spinnerPosition);
            try {
                String selectedDate = sdf.format(accountEntity.accountCreatedDate.getCreatedAt());
                datePicker.setText(selectedDate);
            } catch (NumberFormatException e) {
                executorService = Executors.newCachedThreadPool();
                LogError error = new LogError(e , this ,this);
                executorService.execute(error);
            }

            amountEditText.setText(String.valueOf(accountEntity.accountMoney.getAmount()));
            descEditText.setText(accountEntity.accountDescription.getDesc());
        }
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

    private void editData() {
        Intent resultIntent = getIntent();

        if (accountEntity != null) {
            accountEntity.accountType.setType(selectedType);
            accountEntity.accountMoney.setAmount(selectedAmount);
            accountEntity.accountCreatedDate.setCreatedAt(parsedDate);
            accountEntity.accountDescription.setDesc(selectedDesc);
        }

        if (hasExpense) {
            resultIntent.putExtra(Config.EXPENSE_TABLE_NAME, accountEntity);
        } else {
            resultIntent.putExtra(Config.INCOME_TABLE_NAME, accountEntity);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("update", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> accountIDs = sharedPreferences.getStringSet("accountIDs", new HashSet<>());

        assert accountIDs != null;
        accountIDs.add(accountEntity.accountID.getId());

        editor.putStringSet("accountIDs", accountIDs);

        editor.apply();

        setResult(1, resultIntent);
    }

    @Override
    public void fileStatusListener(String status) {
        executorService.shutdown();
    }
}
