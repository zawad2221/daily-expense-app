package info.devram.dailyexpenses.ui;

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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import info.devram.dailyexpenses.Adapters.ExpenseRecyclerAdapter;
import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.Adapters.RecyclerOnClick;
import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class TodayPageFragment extends Fragment implements RecyclerOnClick{

    public static final String TAG = "TodayFragment";
    private MainActivityViewModel mainActivityViewModel;

    private RecyclerView expenseRecyclerView;
    private RecyclerView incomeRecyclerView;
    private int expenseTotalSum;
    private int incomeTotalSum;

    public TodayPageFragment(MainActivityViewModel viewModel) {
        this.mainActivityViewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_page, container, false);

        /*
         * instantiate view model data object
         *
         * VieModelProvider class is instantiated which takes
         * Store or we can say Activity as their store owner or place
         * where ViewModel object will stored
         *
         * Then we get the Object of Class which extends
         * ViewModel class,
         * i.e MainActivityViewModel
         */

//        MainActivityViewModel mainActivityViewModel = new ViewModelProvider
//                .AndroidViewModelFactory(requireActivity().getApplication())
//                .create(MainActivityViewModel.class);


        incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);
        expenseRecyclerView = view.findViewById(R.id.exp_recycler_view);

        TextView incomeTotalAmountTextView = view.findViewById(R.id.inc_total_amt_txt_view);
        TextView expenseTotalAmountTextView = view.findViewById(R.id.total_exp_amt_txt_view);

        /*
         * observer pattern applied to viewModel object so that
         * data can be observed
         */

        updateIncomeRecyclerView();
        updateExpenseRecyclerView();

        incomeTotalAmountTextView
                .setText(MessageFormat.format("{0} {1}",
                        getResources().getString(R.string.rs_symbol),
                        String.valueOf(incomeTotalSum)));
        
        expenseTotalAmountTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(expenseTotalSum)));

        //expenseRecyclerView.setHasFixedSize(true);
        return view;
    }

    private <T> List<T> getLastThree(List<T> obj) {
        List<T> newList = new ArrayList<>();

        for (int i = obj.size() - 1; i >= (obj.size() - 3); i--) {
            newList.add(obj.get(i));
        }

        return newList;

    }

    private void updateExpenseRecyclerView() {

        if (mainActivityViewModel.getExpenses().getValue().size() > 0) {
            List<Expense> newExpenseList = getLastThree(
                    mainActivityViewModel.getExpenses().getValue()
            );
            expenseTotalSum = getSum(newExpenseList);
            ExpenseRecyclerAdapter expenseRecyclerAdapter = new
                    ExpenseRecyclerAdapter(newExpenseList,this);
            expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            expenseRecyclerView.setAdapter(expenseRecyclerAdapter);
        }


    }

    private void updateIncomeRecyclerView() {
        if (mainActivityViewModel.getIncomes().getValue().size() > 0) {
            List<Income> newIncomeList = getLastThree(
                    mainActivityViewModel.getIncomes().getValue()
            );
            incomeTotalSum = getSum(newIncomeList);
            IncomeRecyclerAdapter incomeRecyclerAdapter = new IncomeRecyclerAdapter(getContext(),
                    newIncomeList);
            incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            incomeRecyclerView.setAdapter(incomeRecyclerAdapter);
        }
    }

    private <T> int getSum(List<T> obj) {
        int totalSum = 0;
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i) instanceof Income) {
                totalSum += ((Income) obj.get(i)).getIncomeAmount();
            }else {
                totalSum += ((Expense) obj.get(i)).getExpenseAmount();
            }
        }
        return totalSum;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onItemClicked(int position) {

    }
}
