package info.devram.dainikhatabook.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dainikhatabook.Adapters.IncomeRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.EditActivity;
import info.devram.dainikhatabook.IncomeActivity;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class IncomePageFragment extends Fragment
        implements RecyclerOnClick, ConfirmModal.ConfirmModalListener {

    private static final String TAG = "IncomePageFragment";

    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;

    private TextView totalIncometextView;
    private MainActivityViewModel mainActivityViewModel;
    private IncomeRecyclerAdapter incomeRecyclerAdapter;

    private int incomeItemAdapterPosition;


    public IncomePageFragment(MainActivityViewModel viewModel) {
        this.mainActivityViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_income_page,
                container, false);

        RecyclerView incomeRecyclerView = view.findViewById(R.id.inc_recycler_view);
        totalIncometextView = view.findViewById(R.id.total_inc_amt_txt_view);
        TextView incomeTitletextView = view.findViewById(R.id.titleTextView);

        incomeTitletextView.setText(MessageFormat.format("{0} {1}",
                "Total", view.getResources().getString(R.string.total_income)));

        incomeRecyclerAdapter = new IncomeRecyclerAdapter(
                mainActivityViewModel.getIncomes(), this);

        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);

        int totalIncome = getSum(mainActivityViewModel.getIncomes());

        totalIncometextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalIncome)));

        FloatingActionButton fab = view.findViewById(R.id.fab_income);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent incomeIntent = new Intent(getActivity(), IncomeActivity.class);

                startActivityForResult(incomeIntent, ADD_REQUEST_CODE);


            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null) {
                    mainActivityViewModel.addIncome(data);
                    setTotalIncome();
                    incomeRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }

        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null) {
                    if (mainActivityViewModel.editIncome(incomeItemAdapterPosition,
                            data)) {
                        incomeRecyclerAdapter.notifyItemChanged(incomeItemAdapterPosition);
                        setTotalIncome();
                    }
                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }
    }


    private int getSum(List<Income> obj) {
        int totalSum = 0;

        if (obj.size() == 0) {
            return totalSum;
        }
        for (int i = 0; i < obj.size(); i++) {
            totalSum += obj.get(i).getIncomeAmount();

        }
        return totalSum;
    }

    private void setTotalIncome() {
        int totalExpenses = getSum(mainActivityViewModel.getIncomes());

        totalIncometextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalExpenses)));
    }

    @Override
    public void onItemClicked(View view, final int position) {
        incomeItemAdapterPosition = position;

        PopupMenu popupMenu = new PopupMenu(view.getContext(), view, Gravity.CENTER);

        popupMenu.getMenuInflater().inflate(R.menu.options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menu_item:
                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        Income selectedObj = mainActivityViewModel.getIncomes()
                                .get(incomeItemAdapterPosition);
                        setIntentData(selectedObj, intent);
                        startActivityForResult(intent, EDIT_REQUEST_CODE);
                        break;
                    case R.id.delete_menu_item:
                        showDialog();
                        break;
                    default:
                }
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {
        if (mainActivityViewModel.deleteIncome(incomeItemAdapterPosition)) {
            incomeRecyclerAdapter.notifyItemRemoved(incomeItemAdapterPosition);
            setTotalIncome();
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    private void setIntentData(Income selectedItem, Intent intentData) {
        intentData.putExtra("title", "Edit Income");
        intentData.putExtra("type", selectedItem.getIncomeType().toLowerCase());
        intentData.putExtra("date", selectedItem.getIncomeDate());
        intentData.putExtra("amount", selectedItem.getIncomeAmount());
        intentData.putExtra("desc", selectedItem.getIncomeDesc());
    }

    private void showDialog() {
        DialogFragment dialogFragment = new ConfirmModal();

        dialogFragment.setTargetFragment(IncomePageFragment.this, 0);
        dialogFragment.show(getParentFragmentManager(), null);

    }
}