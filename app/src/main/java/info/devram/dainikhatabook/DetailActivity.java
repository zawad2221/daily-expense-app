package info.devram.dainikhatabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Adapters.DashBoardRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;
import info.devram.dainikhatabook.ui.ConfirmModal;

public class DetailActivity extends AppCompatActivity
        implements RecyclerOnClick,ConfirmModal.ConfirmModalListener {

    private static final String TAG = "DetailActivity";

    public static final int EDIT_EXP_REQUEST_CODE = 1;
    public static final int EDIT_INC_REQUEST_CODE = 2;
    
    private MainActivityViewModel mainActivityViewModel;
    private List<DashBoardObject> newDashBoardList;
    private RecyclerView recyclerView;
    private DashBoardRecyclerAdapter dashBoardRecyclerAdapter;
    private List<Expense> expenseList;
    private List<Income> incomeList;
    private int itemAdapterPosition;
    private String intentType;
    private boolean hasExpense = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = findViewById(R.id.detailToolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        hasExpense = getIntent().hasExtra(Expense.class.getSimpleName());
        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        mainActivityViewModel.init();

        recyclerView = findViewById(R.id.detailRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    @Override
    protected void onResume() {
        super.onResume();
        intentType = getIntent().getStringExtra("type");
        inflateDashBoardList();
        dashBoardRecyclerAdapter = new DashBoardRecyclerAdapter(newDashBoardList,this);
        recyclerView.setAdapter(dashBoardRecyclerAdapter);
    }

    @Override
    public void onItemClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.detailEditBtn:
                Intent intent = new Intent(DetailActivity.this,EditActivity.class);
                itemAdapterPosition = position;
                if (hasExpense) {
                    Expense expense = expenseList.get(position);
                    intent.putExtra(Expense.class.getSimpleName(),expense);
                    startActivityForResult(intent,EDIT_EXP_REQUEST_CODE);
                }else {
                    Income income = incomeList.get(position);
                    intent.putExtra(Income.class.getSimpleName(),income);
                    startActivityForResult(intent,EDIT_INC_REQUEST_CODE);
                }

                break;
            case R.id.detailDeleteBtn:
                itemAdapterPosition = position;
                ConfirmModal confirmModal = new ConfirmModal("Are You Sure You Want To Delete This",
                        "Alert!" + "\n\n",true,this);
                confirmModal.show(getSupportFragmentManager(),TAG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EXP_REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null) {
                    Expense expense = (Expense) data.getSerializableExtra(Expense.class.getSimpleName());
                    mainActivityViewModel.editExpense(itemAdapterPosition,expense);
                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }
        if (requestCode == EDIT_INC_REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null) {
                    Income income = (Income) data.getSerializableExtra(Income.class.getSimpleName());
                    mainActivityViewModel.editIncome(itemAdapterPosition,income);
                }
            }
        }

    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {
        if (hasExpense) {
            mainActivityViewModel.deleteExpense(itemAdapterPosition);
        }else {
            Log.d(TAG, "onOkClick: " + itemAdapterPosition);
            mainActivityViewModel.deleteIncome(itemAdapterPosition);
        }
        inflateDashBoardList();
        dashBoardRecyclerAdapter.updateData(newDashBoardList);
        dialogFragment.dismiss();

    }

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    private void inflateDashBoardList() {

        newDashBoardList = new ArrayList<>();
        if (hasExpense) {
            expenseList = new ArrayList<>();
            expenseList = mainActivityViewModel.getExpenseByType(intentType);
            for(Expense expense: expenseList) {
                DashBoardObject dashBoardObject = new DashBoardObject();
                dashBoardObject.setTypeObject(expense.getExpenseType());
                dashBoardObject.setDateObject(expense.getExpenseDate());
                dashBoardObject.setAmountObject(expense.getExpenseAmount());
                dashBoardObject.setDescObject(expense.getExpenseDesc());
                dashBoardObject.setIsExpense(true);
                newDashBoardList.add(dashBoardObject);
            }
        }else {
            incomeList = mainActivityViewModel.getIncomeByType(intentType);
            for(Income income: incomeList) {
                DashBoardObject dashBoardObject = new DashBoardObject();
                dashBoardObject.setTypeObject(income.getIncomeType());
                dashBoardObject.setDateObject(income.getIncomeDate());
                dashBoardObject.setAmountObject(income.getIncomeAmount());
                dashBoardObject.setDescObject(income.getIncomeDesc());
                newDashBoardList.add(dashBoardObject);
            }
        }
    }
}