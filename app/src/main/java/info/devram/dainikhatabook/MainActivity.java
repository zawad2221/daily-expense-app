package info.devram.dainikhatabook;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Services.SyncService;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;
import info.devram.dainikhatabook.ui.ConfirmModal;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ConfirmModal.ConfirmModalListener {

    public static final String TAG = "MainActivity";

    public static final int ADD_EXP_REQUEST_CODE = 1;
    public static final int ADD_INC_REQUEST_CODE = 2;

    private MainActivityViewModel mainActivityViewModel;
    private TextView expenseSumTextView;
    private TextView incomeSumTextView;
    private Button settingsButton;
    private Button addExpenseButton;
    private Button addIncomeButton;
    private Button generateReportButton;
    private Button helpButton;
    private Button aboutButton;
    private List<Expense> newExpenseList;
    private List<Income> newIncomeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        mainActivityViewModel.init();

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
        newExpenseList = null;
        newIncomeList = null;
        mainActivityViewModel.setExpenses();
        mainActivityViewModel.setIncomes();

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
                intent.putExtra(Expense.class.getSimpleName(), "add");
                startActivityForResult(intent, ADD_EXP_REQUEST_CODE);
                break;
            case R.id.dashAddIncBtn:
                intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(Income.class.getSimpleName(), "add");
                startActivityForResult(intent, ADD_INC_REQUEST_CODE);
                break;
            case R.id.dashReportBtn:
                ConfirmModal confirmModal = new ConfirmModal("Ability To Generate Reports",
                        "Coming Soon\n", false, this);
                confirmModal.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.dashHelpBtn:
                confirmModal = new ConfirmModal("How to use This app",
                        "Coming Soon\n",false,this);
                confirmModal.show(getSupportFragmentManager(),TAG);
                break;
            case R.id.dashAboutBtn:
                String aboutSummary = String.format(getResources().getString(R.string.about_summary),
                        BuildConfig.VERSION_NAME);
                confirmModal = new ConfirmModal(aboutSummary, "About This App\n",
                        false, MainActivity.this);
                confirmModal.show(getSupportFragmentManager(), TAG);
                break;

        }
    }

    private void populateList() {
        newExpenseList = new ArrayList<>();
        newIncomeList = new ArrayList<>();

        newExpenseList = mainActivityViewModel.getExpenses();
        newIncomeList = mainActivityViewModel.getIncomes();

        List<String> sumList = Util.getSum(newExpenseList, newIncomeList);

        String expSum = String.format(getResources().getString(R.string.total_dashboard_amount),
                sumList.get(0));
        String incSum = String.format(getResources().getString(R.string.total_dashboard_amount),
                sumList.get(1));
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
                expDetailIntent.putExtra(Expense.class.getSimpleName(), "expense");
                startActivity(expDetailIntent);
                break;
            case R.id.income_detail:
                Intent incDetailIntent = new Intent(MainActivity.this, SummaryActivity.class);
                incDetailIntent.putExtra(Income.class.getSimpleName(), "income");
                startActivity(incDetailIntent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {}

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXP_REQUEST_CODE && data != null) {
            Expense expense = (Expense) data.getSerializableExtra(Expense.class.getSimpleName());
            if (expense != null) {
                mainActivityViewModel.addExpense(expense);
            }

        }
        if (requestCode == ADD_INC_REQUEST_CODE && data != null) {
            Income income = (Income) data.getSerializableExtra(Income.class.getSimpleName());
            if (income != null) {
                mainActivityViewModel.addIncome(income);
            }

        }
        populateList();
    }

}