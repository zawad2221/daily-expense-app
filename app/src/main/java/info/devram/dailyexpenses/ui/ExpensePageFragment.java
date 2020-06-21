package info.devram.dailyexpenses.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import info.devram.dailyexpenses.Adapters.ExpenseRecyclerAdapter;
import info.devram.dailyexpenses.Adapters.RecyclerOnClick;
import info.devram.dailyexpenses.ExpenseActivity;
import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class ExpensePageFragment extends Fragment implements RecyclerOnClick {

    private static final String TAG = "ExpensePageFragment";

    private DialogFragment dialog;
    private MainActivityViewModel mainActivityViewModel;

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

        ExpenseRecyclerAdapter expenseRecyclerAdapter = new ExpenseRecyclerAdapter(
                mainActivityViewModel.getExpenses().getValue(),this);

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expenseRecyclerAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_expense);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent expenseIntent = new Intent(getActivity(), ExpenseActivity.class);

                startActivity(expenseIntent);

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
    public void onPause() {
        Log.i(TAG, "onPause started: ");
        mainActivityViewModel.getExpenses().observe(getActivity(),
                new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                Log.i(TAG, "onChanged: " + expenses);
            }
        });
        super.onPause();
        Log.i(TAG, "onPause ended: ");
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop started: ");
        super.onStop();
        Log.i(TAG, "onStop ended: ");
    }

    @Override
    public void onItemClicked(int position) {
        Log.i(TAG, "onItemClicked: " + position);
    }
}
