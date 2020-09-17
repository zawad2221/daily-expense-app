package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.ViewModel.AccountViewModel;

public class SummaryActivity extends AppCompatActivity
        implements RecyclerOnClick {

    //private static final String TAG = "SummaryActivity";

    private AccountViewModel accountViewModel;
    private TextView totalSumTextView;
    private boolean hasClass = false;
    private RecyclerView detailRecyclerView;
    private List<String> totalSum;
    private List<AccountEntity> expenseList;
    private List<AccountEntity> incomeList;

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

        hasClass = getIntent().hasExtra(Config.EXPENSE_TABLE_NAME);

        detailRecyclerView = findViewById(R.id.summary_recycler_view);
        totalSumTextView = findViewById(R.id.detailSumTxtView);

        accountViewModel = AccountViewModel.getInstance(getApplication());

        accountViewModel.init();

        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSumTotal();
        if (hasClass) {

            setTitle("Expense Details");
            ExpenseRecyclerAdapter expRecyclerAdapter = new ExpenseRecyclerAdapter(
                    expenseList,
                    this);
            detailRecyclerView.setAdapter(expRecyclerAdapter);
            totalSumTextView.setText(totalSum.get(0));
        } else {
            setTitle("Income Details");
            IncomeRecyclerAdapter incRecyclerAdapter = new IncomeRecyclerAdapter(
                    incomeList,
                    this);
            detailRecyclerView.setAdapter(incRecyclerAdapter);
            totalSumTextView.setText(totalSum.get(1));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getSumTotal() {

        expenseList = accountViewModel
                .getAccountByTypes(Util.getExpenseTypes(), Config.EXPENSE_TABLE_NAME);

        incomeList = accountViewModel
                .getAccountByTypes(Util.getIncomeTypes(), Config.INCOME_TABLE_NAME);

        totalSum = Util.getSum(accountViewModel.getAccounts(null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position) {

        if (hasClass) {
            AccountEntity accountEntity = expenseList.get(position);

            Intent intent = new Intent(SummaryActivity.this, DetailActivity.class);
            intent.putExtra("type", accountEntity.accountType.getType());
            intent.putExtra(Config.EXPENSE_TABLE_NAME, "expense");
            startActivity(intent);
        } else {
            AccountEntity accountEntity = incomeList.get(position);
            Intent intent = new Intent(SummaryActivity.this, DetailActivity.class);
            intent.putExtra("type", accountEntity.accountType.getType());
            intent.putExtra(Config.INCOME_TABLE_NAME, "income");
            startActivity(intent);
        }
    }

}