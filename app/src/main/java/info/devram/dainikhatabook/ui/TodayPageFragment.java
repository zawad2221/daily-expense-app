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
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import info.devram.dainikhatabook.Adapters.DashBoardRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class TodayPageFragment extends Fragment {

    public static final String TAG = "TodayFragment";
    private MainActivityViewModel mainActivityViewModel;


    private int expenseTotalSum;
    private int incomeTotalSum;
    private TextView netCashinHand;
    private ImageView netCashImageView;
    private CardView netCashCardView;
    private RecyclerView recyclerView;
    private DashBoardRecyclerAdapter dashBoardRecyclerAdapter;
    private List<Income> newIncomeList;
    private List<Expense> newExpenseList;
    private List newDashBoardList;

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

        recyclerView = view.findViewById(R.id.dashboard_recycler_view);
        netCashinHand = view.findViewById(R.id.total_amt_txt_view);
        netCashImageView = view.findViewById(R.id.netcash_image);
        netCashCardView = view.findViewById(R.id.netCash_cardView);

        /*
         * observer pattern applied to viewModel object so that
         * data can be observed
         */

        updateDashboardRecycler();
        updateNetCashTextView();

        return view;
    }

    private void updateDashboardRecycler() {

        populateList();

        dashBoardRecyclerAdapter = new DashBoardRecyclerAdapter(newDashBoardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dashBoardRecyclerAdapter);

    }

    private void populateList() {
        newExpenseList = mainActivityViewModel.getExpenses().getValue();
        newIncomeList = mainActivityViewModel.getIncomes().getValue();

        newDashBoardList = new ArrayList();

        newDashBoardList.addAll(newExpenseList);
        newDashBoardList.addAll(newIncomeList);

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
        netCashinHand.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(netCash)));
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
        Log.i(TAG, "onResume: " + dashBoardRecyclerAdapter.getItemCount());
        dashBoardRecyclerAdapter.updateData(newDashBoardList);
        updateNetCashTextView();
    }
}
