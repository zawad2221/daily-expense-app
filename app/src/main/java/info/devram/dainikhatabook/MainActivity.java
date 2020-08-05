package info.devram.dainikhatabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.util.Log;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Adapters.DashBoardRecyclerAdapter;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.Services.SyncService;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;
import info.devram.dainikhatabook.ui.SelectModal;

public class MainActivity extends AppCompatActivity
        implements SelectModal.OnSelectListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    public static final int ADD_REQUEST_CODE = 1;

    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView recyclerView;
    private List<DashBoardObject> newDashBoardList;
    private SelectModal selectModal;
    private TextView expenseSumTextView;
    private TextView incomeSumTextView;
    private List<Expense> newExpenseList = new ArrayList<>();
    private List<Income> newIncomeList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        mainActivityViewModel.init();

        recyclerView = findViewById(R.id.dashboard_recycler_view);


        expenseSumTextView = findViewById(R.id.dashboardExpAmountTextView);
        incomeSumTextView = findViewById(R.id.dashboardIncAmountTextView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: starts");
        Log.d(TAG, "onResume: " + selectModal);
        if (selectModal == null) {
            selectModal = new SelectModal(this);
        }
        updateDashboardRecycler();
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

    private void updateDashboardRecycler() {

        populateList();

        DashBoardRecyclerAdapter dashBoardRecyclerAdapter = new DashBoardRecyclerAdapter(newDashBoardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dashBoardRecyclerAdapter);
        Log.d(TAG, "recycler view adapter " + dashBoardRecyclerAdapter.getItemCount());

    }

    private void populateList() {
        newDashBoardList = new ArrayList<>();
        newExpenseList = mainActivityViewModel.getExpenses();
        newIncomeList = mainActivityViewModel.getIncomes();

        Log.d(TAG, "expense size " + newExpenseList.size());
        Log.d(TAG, "income size " + newIncomeList.size());


        for(Expense expense: newExpenseList) {
            DashBoardObject dashBoardObject = new DashBoardObject();
            dashBoardObject.setTypeObject(expense.getExpenseType());
            dashBoardObject.setDateObject(expense.getExpenseDate());
            dashBoardObject.setAmountObject(expense.getExpenseAmount());
            dashBoardObject.setDescObject(expense.getExpenseDesc());
            dashBoardObject.setIsExpense(true);
            newDashBoardList.add(dashBoardObject);
        }

        for(Income income: newIncomeList) {
            DashBoardObject dashBoardObject = new DashBoardObject();
            dashBoardObject.setTypeObject(income.getIncomeType());
            dashBoardObject.setDateObject(income.getIncomeDate());
            dashBoardObject.setAmountObject(income.getIncomeAmount());
            dashBoardObject.setDescObject(income.getIncomeDesc());
            newDashBoardList.add(dashBoardObject);
        }

        List<String> sumList = getSum(newExpenseList, newIncomeList);

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
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST_CODE) {

            if (resultCode == 1) {
                if (data != null) {
                    if (data.hasExtra(Expense.class.getSimpleName())) {
                        Expense expense = (Expense) data.getSerializableExtra(Expense.class.getSimpleName());
                        Log.d(TAG, "onActivityResult: " + expense);
                        assert expense != null;
                        mainActivityViewModel.addExpense(expense);
                    }else {
                        Income income = (Income) data.getSerializableExtra(Income.class.getSimpleName());
                        Log.d(TAG, "onActivityResult: " + income);
                        assert income != null;
                        mainActivityViewModel.addIncome(income);
                    }

                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }
    }

    private List<String> getSum(List<Expense> expenseOBJ, List<Income> incomeOBJ) {

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


    @Override
    public void onItemSelected(String selectedItem) {
        Log.d(TAG, "onItemSelected: " + selectedItem);
        if (selectedItem != null) {
            selectModal.dismiss();
            Intent addNewIntent = new Intent(MainActivity.this, AddActivity.class);
            switch (selectedItem) {
                case "expense":
                    addNewIntent.putExtra(Expense.class.getSimpleName(),"add");
                    startActivityForResult(addNewIntent, ADD_REQUEST_CODE);
                    break;

                case "income":
                    addNewIntent.putExtra(Income.class.getSimpleName(),"add");
                    startActivityForResult(addNewIntent, ADD_REQUEST_CODE);
                    break;

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_addNew:
                selectModal.show(getSupportFragmentManager(),TAG);
                break;
            case R.id.navigation_notifications:
                Log.d(TAG, "onNavigationItemSelected: notification ");
                break;
        }
        return false;
    }
}