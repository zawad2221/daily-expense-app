package info.devram.dailyexpenses.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.IncomeActivity;
import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class IncomePageFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;



    public IncomePageFragment(MainActivityViewModel viewModel) {
        // Required empty public constructor
        this.mainActivityViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_income_page,
                container, false);

        RecyclerView incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);
        TextView totalIncometextView = view.findViewById(R.id.total_inc_amt_txt_view);
        TextView incomeTitletextView = view.findViewById(R.id.titleTextView);

        incomeTitletextView.setText(MessageFormat.format("{0} {1}",
                "Total",view.getResources().getString(R.string.total_income)));

        IncomeRecyclerAdapter incomeRecyclerAdapter = new IncomeRecyclerAdapter(
                view.getContext(),
                mainActivityViewModel.getIncomes().getValue());

        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);

        int totalIncome = getSum(mainActivityViewModel.getIncomes().getValue());

        totalIncometextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalIncome)));

        FloatingActionButton fab = view.findViewById(R.id.fab_income);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(view.getContext(),
                R.array.income_type, android.R.layout.simple_spinner_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent incomeIntent = new Intent(getActivity(), IncomeActivity.class);

                startActivity(incomeIntent);

//                dialog = new SaveDataDialog(adapter,"Enter Income");
//                dialog.show(getParentFragmentManager(),null);

            }
        });

        return view;
    }
    private int getSum(List<Income> obj) {
        int totalSum = 0;
        for (int i = 0; i < obj.size(); i++) {
            totalSum += obj.get(i).getIncomeAmount();

        }
        return totalSum;
    }
}