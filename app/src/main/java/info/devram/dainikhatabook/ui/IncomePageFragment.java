package info.devram.dainikhatabook.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.IncomeActivity;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class IncomePageFragment extends Fragment {

    private static final String TAG = "IncomePageFragment";

    public static final int REQUEST_CODE = 1;
    private TextView totalIncometextView;
    private MainActivityViewModel mainActivityViewModel;
    private IncomeRecyclerAdapter incomeRecyclerAdapter;


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
        totalIncometextView = view.findViewById(R.id.total_inc_amt_txt_view);
        TextView incomeTitletextView = view.findViewById(R.id.titleTextView);

        incomeTitletextView.setText(MessageFormat.format("{0} {1}",
                "Total",view.getResources().getString(R.string.total_income)));

        incomeRecyclerAdapter = new IncomeRecyclerAdapter(
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

                startActivityForResult(incomeIntent,REQUEST_CODE);

//                dialog = new SaveDataDialog(adapter,"Enter Income");
//                dialog.show(getParentFragmentManager(),null);

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
                    Income income = new Income();
                    income.setIncomeType(data.getStringExtra("type"));
                    income.setIncomeDate(data.getStringExtra("date"));
                    income.setIncomeAmount(data.getIntExtra("amount",0));
                    income.setIncomeDesc(data.getStringExtra("desc"));
                    if (mainActivityViewModel.addIncome(income)) {
                        incomeRecyclerAdapter.notifyDataSetChanged();
                        setTotalIncome();
                    }
                }else {
                    Log.e(TAG, "onActivityResult: intent data is null " );
                }
            }
        }
    }
    private int getSum(List<Income> obj) {
        int totalSum = 0;
        for (int i = 0; i < obj.size(); i++) {
            totalSum += obj.get(i).getIncomeAmount();

        }
        return totalSum;
    }

    private void setTotalIncome() {
        int totalExpenses = getSum(mainActivityViewModel.getIncomes().getValue());

        totalIncometextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalExpenses)));
    }
}