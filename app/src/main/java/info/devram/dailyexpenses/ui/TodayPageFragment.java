package info.devram.dailyexpenses.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.devram.dailyexpenses.Adapters.ExpenseRecyclerAdapter;
import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.Repository.IncomeRepository;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class TodayPageFragment extends Fragment {

    public static final String TAG = "TodayFragment";

    private IncomeRecyclerAdapter incomeRecyclerAdapter;
    private ExpenseRecyclerAdapter expenseRecyclerAdapter;
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView incomeRecyclerView;
    private IncomeRepository incomeRepository;
    //private RecyclerView expenseRecyclerView;

    public TodayPageFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_page,container,false);

        /*
        * testing to see if repository populates adapter
         */

        incomeRepository = new IncomeRepository();



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

        mainActivityViewModel = new ViewModelProvider(this)
                .get(MainActivityViewModel.class);

        mainActivityViewModel.init();

        incomeRecyclerAdapter = new IncomeRecyclerAdapter(view.getContext(),mainActivityViewModel.getIncomes().getValue());

        incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);
        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);


        /*
         * observer pattern applied to viewModel object so that
         * data can be observed
         */

//        Log.i(TAG, "onCreate: " + mainActivityViewModel.getIncomes().getValue());
//
        mainActivityViewModel.getIncomes().observe(getViewLifecycleOwner(), new Observer<List<Income>>() {
            // this method used for observing any changes in ViewModel Data
            // in our case Income class object

            @Override
            public void onChanged(List<Income> incomes) {


            }
        });


        //expenseRecyclerView = view.findViewById(R.id.exp_recycler_view);




//        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(mainActivityViewModel
//                .getExpenses().getValue());

        incomeRecyclerView.setHasFixedSize(false);
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));




//        expenseRecyclerView.setHasFixedSize(true);
//        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        expenseRecyclerView.setAdapter(expenseRecyclerAdapter);

        return view;
    }



}
