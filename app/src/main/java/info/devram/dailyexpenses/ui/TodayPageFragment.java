package info.devram.dailyexpenses.ui;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import info.devram.dailyexpenses.Adapters.ExpenseRecyclerAdapter;
import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class TodayPageFragment extends Fragment {

    public static final String TAG = "TodayFragment";
    private MainActivityViewModel mainActivityViewModel;

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


        List<Income> incomeList = mainActivityViewModel.getIncomes().getValue();

        List<Income> newIncomeList = getLastThree(incomeList);

        IncomeRecyclerAdapter incomeRecyclerAdapter = new IncomeRecyclerAdapter(view.getContext(),
                newIncomeList);

        RecyclerView incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);
        TextView incomeTotalAmountTextView = view.findViewById(R.id.inc_total_amt_txt_view);
        TextView expenseTotalAmountTextView = view.findViewById(R.id.total_exp_amt_txt_view);
        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);


        /*
         * observer pattern applied to viewModel object so that
         * data can be observed
         */

//        Log.i(TAG, "onCreate: " + mainActivityViewModel.getIncomes().getValue());
//
        mainActivityViewModel.getIncomes().observe(getViewLifecycleOwner(),
                new Observer<List<Income>>() {
                    // this method used for observing any changes in ViewModel Data
                    // in our case Income class object

                    @Override
                    public void onChanged(List<Income> incomes) {

                    }
                });

        int totalSum = 0;
        for (int i = 0; i < newIncomeList.size(); i++) {
            totalSum += newIncomeList.get(i).getIncomeAmount();
        }


        incomeTotalAmountTextView
                .setText(MessageFormat.format("{0} {1}",
                        getResources().getString(R.string.rs_symbol),
                        String.valueOf(totalSum)));

        RecyclerView expenseRecyclerView = view.findViewById(R.id.exp_recycler_view);

        List<Expense> expenseList = mainActivityViewModel.getExpenses().getValue();

        List<Expense> newExpenseList = getLastThree(expenseList);

        totalSum = 0;
        for (int i = 0; i < newExpenseList.size(); i++) {
            totalSum += newExpenseList.get(i).getExpenseAmount();
        }


        ExpenseRecyclerAdapter expenseRecyclerAdapter = new ExpenseRecyclerAdapter(newExpenseList);

        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        expenseTotalAmountTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalSum)));


        //expenseRecyclerView.setHasFixedSize(true);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expenseRecyclerAdapter);

        return view;
    }

    private <T> List<T> getLastThree(List<T> obj) {
        List<T> newList = new ArrayList<>();

        for (int i = obj.size() - 1; i >= (obj.size() - 3); i--) {
            newList.add(obj.get(i));
        }

        return newList;

    }
}
