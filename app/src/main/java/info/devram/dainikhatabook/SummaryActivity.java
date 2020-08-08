package info.devram.dainikhatabook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class SummaryActivity extends AppCompatActivity
        implements RecyclerOnClick {

    private static final String TAG = "SummaryActivity";

    private MainActivityViewModel mainActivityViewModel;
    private TextView totalSumTextView;
    private ExpenseRecyclerAdapter expRecyclerAdapter;
    private IncomeRecyclerAdapter incRecyclerAdapter;
    private int editItemAdapterPosition;
    private boolean hasClass = false;
    private RecyclerView detailRecyclerView;
    private List<String> totalSum;
    private List<Expense> expenseList;
    private List<Income> incomeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = findViewById(R.id.toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        hasClass = getIntent().hasExtra(Expense.class.getSimpleName());

        detailRecyclerView = findViewById(R.id.summary_recycler_view);
        totalSumTextView = findViewById(R.id.detailSumTxtView);

        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        mainActivityViewModel.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSumTotal();
        if (hasClass) {
            setTitle("Expense Details");

            expRecyclerAdapter = new ExpenseRecyclerAdapter(
                    expenseList,
                    this);

            detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            detailRecyclerView.setAdapter(expRecyclerAdapter);
            totalSumTextView.setText(totalSum.get(0));
        }else{
            setTitle("Income Details");
            incRecyclerAdapter = new IncomeRecyclerAdapter(
                    mainActivityViewModel.getIncomes(),
                    this);
//            expenseTitleTextView.setText(MessageFormat.format("{0} {1}",
//                    "Total", getResources().getString(R.string.total_expense)));

            detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            detailRecyclerView.setAdapter(incRecyclerAdapter);
            totalSumTextView.setText(totalSum.get(1));
        }

    }

    private void getSumTotal() {
        List<String> expTypes = new LinkedList<>(Arrays.asList(
                getResources().getStringArray(R.array.expense_type)));
        expenseList = mainActivityViewModel.getExpenseSummaryType(expTypes);
        incomeList = mainActivityViewModel.getIncomes();
        totalSum = Util.getSum(expenseList,incomeList);
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

    @Override
    public void onItemClicked(View view, int position) {

        if (hasClass) {
            Expense expense = expenseList.get(position);
            Intent intent = new Intent(SummaryActivity.this,DetailActivity.class);
            intent.putExtra("type",expense.getExpenseType());
            startActivity(intent);
        }else {
            Income income = incomeList.get(position);
            Intent intent = new Intent(SummaryActivity.this,DetailActivity.class);
            intent.putExtra("type",income.getIncomeType());
            startActivity(intent);
        }
    }

}