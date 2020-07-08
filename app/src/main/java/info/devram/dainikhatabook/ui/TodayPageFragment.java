package info.devram.dainikhatabook.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class TodayPageFragment extends Fragment implements RecyclerOnClick {

    public static final String TAG = "TodayFragment";
    private MainActivityViewModel mainActivityViewModel;

    private RecyclerView expenseRecyclerView;
    private RecyclerView incomeRecyclerView;
    private int expenseTotalSum;
    private int incomeTotalSum;
    private TextView netCashinHand;
    private TextView expenseTotalAmountTextView;
    private TextView incomeTotalAmountTextView;
    private ImageView netCashImageView;
    private CardView netCashCardView;
    private IncomeRecyclerAdapter incomeRecyclerAdapter;
    private ExpenseRecyclerAdapter expenseRecyclerAdapter;
    private int incomeCount;
    private int expenseCount;
    private List<Income> newIncomeList;
    private List<Expense> newExpenseList;

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
        expenseRecyclerView = view.findViewById(R.id.todayFrag_exp_recycle_view);

        incomeTotalAmountTextView = view.findViewById(R.id.total_inc_amt_txt_view);
        expenseTotalAmountTextView = view.findViewById(R.id.total_exp_amt_txt_view);
        TextView expenseTitleTextView = view.findViewById(R.id.title_exp_txtView);
        TextView incomeTitletextView = view.findViewById(R.id.today_inc_titleTextView);
        netCashinHand = view.findViewById(R.id.total_amt_txt_view);
        netCashImageView = view.findViewById(R.id.netcash_image);
        netCashCardView = view.findViewById(R.id.netCash_cardView);
        /*
         * observer pattern applied to viewModel object so that
         * data can be observed
         */

        updateIncomeRecyclerView();
        updateExpenseRecyclerView();

        expenseTitleTextView.setText(MessageFormat.format("{0} {1}",
                "Today", view.getResources().getString(R.string.total_expense)));

        incomeTitletextView.setText(MessageFormat.format("{0} {1}",
                "Today", view.getResources().getString(R.string.total_income)));

        incomeCount = mainActivityViewModel.getIncomes().getValue().size();
        expenseCount = mainActivityViewModel.getExpenses().getValue().size();

        return view;
    }

    private <T> List<T> getLastThree(List<T> obj) {
        List<T> newList = new ArrayList<>();

        if (obj.size() >= 3) {
            for (int i = obj.size() - 1; i >= (obj.size() - 3); i--) {

                newList.add(obj.get(i));
            }
        } else {
            return obj;
        }
        return newList;
    }

    private void updateExpenseRecyclerView() {

        if (mainActivityViewModel.getExpenses().getValue().size() > 0) {

            newExpenseList = getLastThree(
                    mainActivityViewModel.getExpenses().getValue()
            );


            expenseTotalSum = getSum(newExpenseList);
            expenseRecyclerAdapter = new
                    ExpenseRecyclerAdapter(newExpenseList, this);
            expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            expenseRecyclerView.setAdapter(expenseRecyclerAdapter);

            expenseTotalAmountTextView.setText(MessageFormat.format("{0} {1}",
                    getResources().getString(R.string.rs_symbol),
                    String.valueOf(expenseTotalSum)));


        }

    }

    private void updateIncomeRecyclerView() {

        if (mainActivityViewModel.getIncomes().getValue().size() > 0) {
            newIncomeList = getLastThree(
                    mainActivityViewModel.getIncomes().getValue()
            );
            incomeTotalSum = getSum(newIncomeList);
            incomeRecyclerAdapter = new IncomeRecyclerAdapter(newIncomeList, this);
            incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            incomeRecyclerView.setAdapter(incomeRecyclerAdapter);

            incomeTotalAmountTextView
                    .setText(MessageFormat.format("{0} {1}",
                            getResources().getString(R.string.rs_symbol),
                            String.valueOf(incomeTotalSum)));


        }

    }

    private <T> int getSum(List<T> obj) {
        int totalSum = 0;
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i) instanceof Income) {
                totalSum += ((Income) obj.get(i)).getIncomeAmount();
            } else {
                totalSum += ((Expense) obj.get(i)).getExpenseAmount();
            }
        }
        return totalSum;
    }

    private void updateNetCashTextView() {
        expenseTotalSum = getSum(mainActivityViewModel.getExpenses().getValue());
        incomeTotalSum = getSum(mainActivityViewModel.getIncomes().getValue());


        int netCash = incomeTotalSum - expenseTotalSum;

        if (netCash == 0) {
            netCashImageView.setImageResource(R.drawable.ic_balance);
            netCashCardView.setCardBackgroundColor(Color.WHITE);
        } else if (netCash > 0) {
            netCashImageView.setImageResource(R.drawable.ic_balance_left);
            netCashCardView.setCardBackgroundColor(getResources().getColor(R.color.alterAccent));
        } else {
            netCashImageView.setImageResource(R.drawable.ic_balance_right);
            netCashCardView.setCardBackgroundColor(getResources().getColor(R.color.error));
        }
        incomeTotalAmountTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(incomeTotalSum)));
        expenseTotalAmountTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(expenseTotalSum)));
        netCashinHand.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(incomeTotalSum - expenseTotalSum)));
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivityViewModel.getIncomes().observe(requireActivity(),
                new Observer<List<Income>>() {
            @Override
            public void onChanged(List<Income> incomes) {

                if (incomeRecyclerAdapter == null) {
                    updateIncomeRecyclerView();
                }
                if (incomes.size() > 0 && incomes.size() < 4) {
                    incomeRecyclerAdapter.notifyItemChanged(0);
                }

            }
        });

        mainActivityViewModel.getExpenses().observe(requireActivity(),
                new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                newExpenseList = getLastThree(expenses);

                if (expenseRecyclerAdapter == null) {
                    updateExpenseRecyclerView();
                }
                if (expenses.size() > 0 && expenses.size() < 4) {
                    expenseRecyclerAdapter.notifyItemChanged(0,
                            newExpenseList.get(newExpenseList.size() - 1));
                }
            }
        });

        updateNetCashTextView();

    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.i(TAG, "onItemClicked: recycler view " + position);
    }
}
