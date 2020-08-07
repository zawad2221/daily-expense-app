package info.devram.dainikhatabook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class DetailsActivity extends AppCompatActivity implements RecyclerOnClick {

    private static final String TAG = "EditActivity";

    private MainActivityViewModel mainActivityViewModel;
    private TextView totalExpenseTextView;
    private ExpenseRecyclerAdapter expRecyclerAdapter;
    private IncomeRecyclerAdapter incRecyclerAdapter;
    private List<Expense> syncList = new ArrayList<>();
    private int editItemAdapterPosition;
    private boolean hasClass = false;
    private RecyclerView detailRecyclerView;
    private TextView expenseTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

        detailRecyclerView = findViewById(R.id.detail_recycler_view);
        totalExpenseTextView = findViewById(R.id.total_exp_amt_txt_view);
        expenseTitleTextView = findViewById(R.id.title_exp_txtView);

        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        mainActivityViewModel.init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setTotalExpense();
        if (hasClass) {
            setTitle("Expense Details");
            List<String> expTypes = new LinkedList<>(Arrays.asList(
                    getResources().getStringArray(R.array.expense_type)));

            expRecyclerAdapter = new ExpenseRecyclerAdapter(
                    mainActivityViewModel.getExpenseSummaryType(expTypes),
                    this);

            detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            detailRecyclerView.setAdapter(expRecyclerAdapter);
        }else{
            setTitle("Income Details");
            incRecyclerAdapter = new IncomeRecyclerAdapter(
                    mainActivityViewModel.getIncomes(),
                    this);
//            expenseTitleTextView.setText(MessageFormat.format("{0} {1}",
//                    "Total", getResources().getString(R.string.total_expense)));

            detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            detailRecyclerView.setAdapter(incRecyclerAdapter);
        }

    }

    private void setTotalExpense() {
        int totalExpenses = getSum(mainActivityViewModel.getExpenses());

        totalExpenseTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalExpenses)));
    }

    private int getSum(List<Expense> obj) {

        int totalSum = 0;

        if (obj.size() == 0) {
            return totalSum;
        }
        for (int i = 0; i < obj.size(); i++) {

            totalSum += obj.get(i).getExpenseAmount();

        }

        return totalSum;
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
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position) {

    }
}