package info.devram.dainikhatabook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.ExpenseActivity;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class ExpensePageFragment extends Fragment{

    private static final String TAG = "ExpensePageFragment";

    public static final int REQUEST_CODE = 1;
    private MainActivityViewModel mainActivityViewModel;
    private TextView totalExpenseTextView;
    private ExpenseRecyclerAdapter expRecyclerAdapter;

    public ExpensePageFragment(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense_page,container,false);

        RecyclerView expenseRecyclerView = view.findViewById(R.id.exp_recycler_view);
        totalExpenseTextView = view.findViewById(R.id.total_exp_amt_txt_view);
        TextView expenseTitleTextView = view.findViewById(R.id.title_exp_txtView);

        expenseTitleTextView.setText(MessageFormat.format("{0} {1}",
                "Total",view.getResources().getString(R.string.total_expense)));

        expRecyclerAdapter = new ExpenseRecyclerAdapter(
                view.getContext(),
                mainActivityViewModel.getExpenses().getValue());

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expRecyclerAdapter);

        setTotalExpense();

        FloatingActionButton fab = view.findViewById(R.id.fab_expense);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent expenseIntent = new Intent(getActivity(), ExpenseActivity.class);

                startActivityForResult(expenseIntent,REQUEST_CODE);

//                dialog = new SaveDataDialog(adapter,"Enter Expenses");
//
//                dialog.show(getParentFragmentManager(),null);
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null){
                    Expense expense = new Expense();
                    expense.setExpenseType(data.getStringExtra("type"));
                    expense.setExpenseDate(data.getStringExtra("date"));
                    expense.setExpenseAmount(data.getIntExtra("amount",0));
                    expense.setExpenseDesc(data.getStringExtra("desc"));
                    if (mainActivityViewModel.addExpense(expense)) {
                        expRecyclerAdapter.notifyDataSetChanged();
                        setTotalExpense();
                    }
                }else {
                    Log.e(TAG, "onActivityResult: intent data is null " );
                }
            }
        }
    }

    private int getSum(List<Expense> obj) {
        int totalSum = 0;
        for (int i = 0; i < obj.size(); i++) {
            totalSum += obj.get(i).getExpenseAmount();

        }
        return totalSum;
    }

    private void setTotalExpense() {
        int totalExpenses = getSum(mainActivityViewModel.getExpenses().getValue());

        totalExpenseTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalExpenses)));
    }
}
