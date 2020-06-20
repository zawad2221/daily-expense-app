package info.devram.dailyexpenses.ui;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import info.devram.dailyexpenses.Adapters.IncomeRecyclerAdapter;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class IncomePageFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;
    private DialogFragment dialog;



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

        IncomeRecyclerAdapter incomeRecyclerAdapter = new IncomeRecyclerAdapter(
                view.getContext(),
                mainActivityViewModel.getIncomes().getValue());

        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_income);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(view.getContext(),
                R.array.income_type, android.R.layout.simple_spinner_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new SaveDataDialog(adapter,"Enter Income");
                dialog.show(getParentFragmentManager(),null);

            }
        });

        return view;
    }
}