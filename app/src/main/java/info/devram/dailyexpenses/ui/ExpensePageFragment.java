package info.devram.dailyexpenses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import info.devram.dailyexpenses.Adapters.ExpenseRecyclerAdapter;
import info.devram.dailyexpenses.R;
import info.devram.dailyexpenses.ViewModel.MainActivityViewModel;

public class ExpensePageFragment extends Fragment {

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
                mainActivityViewModel.getExpenses().getValue());

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expenseRecyclerAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new SaveDataDialog();

                //dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.show(getParentFragmentManager(),null);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        return view;
    }
}
