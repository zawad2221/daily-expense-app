package info.devram.dainikhatabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Services.SyncService;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;
import info.devram.dainikhatabook.ui.ConfirmModal;
import info.devram.dainikhatabook.ui.SelectModal;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,ConfirmModal.ConfirmModalListener {

    public static final String TAG = "MainActivity";


    private MainActivityViewModel mainActivityViewModel;
    //private List<DashBoardObject> newDashBoardList;
    private SelectModal selectModal;
    private TextView expenseSumTextView;
    private TextView incomeSumTextView;
    private Button settingsButton;
    private Button addExpenseButton;
    private Button addIncomeButton;
    private Button generateReportButton;
    private Button helpButton;
    private Button aboutButton;
    private ConfirmModal confirmModal;


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
        boolean isBackupEnabled = sharedPreferences.getBoolean("backup",false);

        SyncService syncService = new SyncService(this);
        //Log.d(TAG, "onPostResume: pending jobs " + syncService.getAllJobs());
        if (isBackupEnabled) {

            if (syncService.getAllJobs().size() == 0) {
                syncService.scheduleJob();
            }
        }else {
            if (syncService.getAllJobs().size() > 0) {
                syncService.cancelJob();
            }

        }
        Log.d(TAG, "onResume: ends " + selectModal);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: starts " + selectModal);
        selectModal = null;
        Log.d(TAG, "onPause: ends " + selectModal);
    }

    private void setupWidgets() {

        populateList();
        settingsButton.setOnClickListener(this);
        addExpenseButton.setOnClickListener(this);
        addIncomeButton.setOnClickListener(this);
        generateReportButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

//        DashBoardRecyclerAdapter dashBoardRecyclerAdapter = new DashBoardRecyclerAdapter(newDashBoardList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(dashBoardRecyclerAdapter);
//        Log.d(TAG, "recycler view adapter " + dashBoardRecyclerAdapter.getItemCount());

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.dashSettingBtn:
                intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.dashNewExpBtn:
                intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(Expense.class.getSimpleName(),"add");
                startActivity(intent);
                break;
            case R.id.dashAddIncBtn:
                intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(Income.class.getSimpleName(),"add");
                startActivity(intent);
                break;
            case R.id.dashReportBtn:
                confirmModal = new ConfirmModal("Ability To Generate Reports",
                        "Coming Soon\n",false,this);
                confirmModal.show(getSupportFragmentManager(),TAG);
                break;
            case R.id.dashHelpBtn:
                confirmModal = new ConfirmModal("How to use This app",
                        "Coming Soon\n",false,this);
                confirmModal.show(getSupportFragmentManager(),TAG);
                break;
            case R.id.dashAboutBtn:
                String aboutSummary = String.format(getResources().getString(R.string.about_summary),
                        BuildConfig.VERSION_NAME);
                confirmModal = new ConfirmModal(aboutSummary,"About This App\n",
                        false,MainActivity.this);
                confirmModal.show(getSupportFragmentManager(),TAG);
                break;

        }
    }

    private void populateList() {
        //newDashBoardList = new ArrayList<>();
        List<Expense> newExpenseList = mainActivityViewModel.getExpenses();
        List<Income> newIncomeList = mainActivityViewModel.getIncomes();

        Log.d(TAG, "expense size " + newExpenseList.size());
        Log.d(TAG, "income size " + newIncomeList.size());


//        for(Expense expense: newExpenseList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setTypeObject(expense.getExpenseType());
//            dashBoardObject.setDateObject(expense.getExpenseDate());
//            dashBoardObject.setAmountObject(expense.getExpenseAmount());
//            dashBoardObject.setDescObject(expense.getExpenseDesc());
//            dashBoardObject.setIsExpense(true);
//            newDashBoardList.add(dashBoardObject);
//        }
//
//        for(Income income: newIncomeList) {
//            DashBoardObject dashBoardObject = new DashBoardObject();
//            dashBoardObject.setTypeObject(income.getIncomeType());
//            dashBoardObject.setDateObject(income.getIncomeDate());
//            dashBoardObject.setAmountObject(income.getIncomeAmount());
//            dashBoardObject.setDescObject(income.getIncomeDesc());
//            newDashBoardList.add(dashBoardObject);
//        }

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
                Intent expDetailIntent = new Intent(MainActivity.this, DetailsActivity.class);
                expDetailIntent.putExtra(Expense.class.getSimpleName(),"expense");
                startActivity(expDetailIntent);
                break;
            case R.id.income_detail:
                Intent incDetailIntent = new Intent(MainActivity.this,DetailsActivity.class);
                incDetailIntent.putExtra(Income.class.getSimpleName(),"income");
                startActivity(incDetailIntent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {

    }

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

}