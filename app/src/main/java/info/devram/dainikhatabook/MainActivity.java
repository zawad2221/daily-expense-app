package info.devram.dainikhatabook;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.devram.dainikhatabook.Core.MyApp;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Interfaces.GenerateReportListener;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Services.ExcelCreate;
import info.devram.dainikhatabook.Services.PdfCreate;
import info.devram.dainikhatabook.Services.SyncService;
import info.devram.dainikhatabook.Values.AccountRepoType;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;
import info.devram.dainikhatabook.ui.ConfirmModal;
import info.devram.dainikhatabook.ui.SelectModal;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ConfirmModal.ConfirmModalListener,
        GenerateReportListener, SelectModal.OnSelectListener {

    public static final String TAG = "MainActivity";

    public static final int ADD_EXP_REQUEST_CODE = 1;
    public static final int ADD_INC_REQUEST_CODE = 2;
    public static final int CREATE_FILE = 1;

    private AccountViewModel accountViewModel;
    private TextView expenseSumTextView;
    private TextView incomeSumTextView;
    private Button settingsButton;
    private Button addExpenseButton;
    private Button addIncomeButton;
    private Button generateReportButton;
    private Button helpButton;
    private Button aboutButton;
    private String reportSelectedItem;
    private SelectModal selectModal;
    private List<AccountEntity> accountEntities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountViewModel = AccountViewModel.getInstance(getApplication());

        accountViewModel.init();

        expenseSumTextView = findViewById(R.id.dashboardExpAmountTextView);
        incomeSumTextView = findViewById(R.id.dashboardIncAmountTextView);
        settingsButton = findViewById(R.id.dashSettingBtn);
        addExpenseButton = findViewById(R.id.dashNewExpBtn);
        addIncomeButton = findViewById(R.id.dashAddIncBtn);
        generateReportButton = findViewById(R.id.dashReportBtn);
        helpButton = findViewById(R.id.dashHelpBtn);
        aboutButton = findViewById(R.id.dashAboutBtn);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupWidgets();
        getUserAccount();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean isBackupEnabled = sharedPreferences.getBoolean("backup", false);
        SyncService syncService = new SyncService(this);
        if (isBackupEnabled) {

            if (syncService.getAllJobs().size() == 0) {
                syncService.scheduleJob();
            }
        } else {
            if (syncService.getAllJobs().size() > 0) {
                syncService.cancelJob();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupWidgets() {

        populateList();
        settingsButton.setOnClickListener(this);
        addExpenseButton.setOnClickListener(this);
        addIncomeButton.setOnClickListener(this);
        generateReportButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.dashSettingBtn:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.dashNewExpBtn:
                intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(Config.EXPENSE_TABLE_NAME, "add");
                startActivityForResult(intent, ADD_EXP_REQUEST_CODE);
                break;
            case R.id.dashAddIncBtn:
                intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(Config.INCOME_TABLE_NAME, "add");
                startActivityForResult(intent, ADD_INC_REQUEST_CODE);
                break;
            case R.id.dashReportBtn:
                String[] array = getResources().getStringArray(R.array.report_generate);
                selectModal = new SelectModal(array, this);
                selectModal.show(getSupportFragmentManager(), null);
                break;
            case R.id.dashHelpBtn:
                intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.dashAboutBtn:
                String aboutSummary = String.format(getResources().getString(R.string.about_summary),
                        BuildConfig.VERSION_NAME);
                ConfirmModal confirmModal = new ConfirmModal(aboutSummary, "About This App\n",
                        false, MainActivity.this);
                confirmModal.show(getSupportFragmentManager(), null);
                break;

        }
    }

    private void populateList() {
        accountEntities = new ArrayList<>();
        accountEntities = accountViewModel.getAccounts(null);

        assert accountEntities != null;
        List<String> sumList = Util.getSum(accountEntities);

        String expSum = String.format(getResources().
                        getString(R.string.total_dashboard_amount),sumList.get(0));
        String incSum = String.format(getResources().
                        getString(R.string.total_dashboard_amount),sumList.get(1));

        expenseSumTextView.setText(expSum);
        incomeSumTextView.setText(incSum);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.expense_detail:
                Intent expDetailIntent = new Intent(MainActivity.this, SummaryActivity.class);
                expDetailIntent.putExtra(Config.EXPENSE_TABLE_NAME, "expense");
                startActivity(expDetailIntent);
                break;
            case R.id.income_detail:
                Intent incDetailIntent = new Intent(MainActivity.this, SummaryActivity.class);
                incDetailIntent.putExtra(Config.INCOME_TABLE_NAME, "income");
                startActivity(incDetailIntent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {
    }

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXP_REQUEST_CODE && data != null) {
            AccountEntity accountEntity = (AccountEntity) data.getSerializableExtra(Config.EXPENSE_TABLE_NAME);
            if (accountEntity != null) {
                AccountRepoType accountRepoType = new AccountRepoType(null);
                accountEntity.setAccountRepoType(accountRepoType);
                accountViewModel.addAccount(accountEntity);
            }

        }
        if (requestCode == ADD_INC_REQUEST_CODE && data != null) {
            AccountEntity accountEntity = (AccountEntity) data.getSerializableExtra(Config.INCOME_TABLE_NAME);
            if (accountEntity != null) {
                AccountRepoType accountRepoType = new AccountRepoType(Config.INCOME_TABLE_NAME);
                accountEntity.setAccountRepoType(accountRepoType);
                accountViewModel.addAccount(accountEntity);
            }

        }
        populateList();
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (data != null) {
                uri = data.getData();
                try {
                    assert uri != null;
                    DocumentFile fileURI = DocumentFile.fromTreeUri(this, uri);
                    assert fileURI != null;
                    DocumentFile file;
                    if (reportSelectedItem.equalsIgnoreCase("excel")) {
                        file = fileURI.createFile("application/vnd.ms-excel", "backup");
                    } else {
                        file = fileURI.createFile("application/pdf", "backup");
                    }
                    assert file != null;
                    OutputStream outputStream = getContentResolver().openOutputStream(file.getUri());
                    ExecutorService executorService = Executors.newCachedThreadPool();
                    ExcelCreate excelCreate;
                    PdfCreate pdfCreate;
                    if (reportSelectedItem.equalsIgnoreCase("excel")) {
                        excelCreate = new ExcelCreate(accountEntities, outputStream, this);
                        executorService.execute(excelCreate);
                    } else {
                        pdfCreate = new PdfCreate(accountEntities, outputStream, this);
                        executorService.execute(pdfCreate);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void getUserAccount() {
        int hasGetReadContactsPermission = ContextCompat.checkSelfPermission(
                MainActivity.this, READ_CONTACTS);
        if (hasGetReadContactsPermission == PackageManager.PERMISSION_GRANTED) {
            AccountManager am = AccountManager.get(MainActivity.this);
            Account[] accounts = am.getAccountsByType("com.google");

            if (accounts.length > 0) {
                Log.d(TAG, "getUserAccount: " + accounts.length);
                Log.d(TAG, "getUserAccount: " + accounts[0].name);
                SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("account", accounts[0].name);
                editor.apply();
            }
        }

    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onReportGenerated(String message, STATUS_CODE code) {
        View view = findViewById(android.R.id.content);
        if (code == STATUS_CODE.OK) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(String selectedItem) {
        reportSelectedItem = selectedItem;
        selectModal.dismiss();
        accountEntities = accountViewModel.getAccounts(null);
        assert accountEntities != null;
        if (accountEntities.size() > 0) {
            createFile();
        }
        if (selectModal != null) {

            selectModal = null;
        }
    }
}