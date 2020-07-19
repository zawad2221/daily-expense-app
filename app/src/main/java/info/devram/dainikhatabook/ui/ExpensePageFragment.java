package info.devram.dainikhatabook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.List;

import info.devram.dainikhatabook.Adapters.ExpenseRecyclerAdapter;
import info.devram.dainikhatabook.Adapters.RecyclerOnClick;
import info.devram.dainikhatabook.EditActivity;
import info.devram.dainikhatabook.ExpenseActivity;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.R;
import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;

public class ExpensePageFragment extends Fragment
        implements RecyclerOnClick, ConfirmModal.ConfirmModalListener {

    private static final String TAG = "ExpensePageFragment";

    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;

    private MainActivityViewModel mainActivityViewModel;
    private TextView totalExpenseTextView;
    private ExpenseRecyclerAdapter expRecyclerAdapter;

    private int editItemAdapterPosition;

    public ExpensePageFragment(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense_page, container, false);

        RecyclerView expenseRecyclerView = view.findViewById(R.id.exp_recycler_view);
        totalExpenseTextView = view.findViewById(R.id.total_exp_amt_txt_view);
        TextView expenseTitleTextView = view.findViewById(R.id.title_exp_txtView);

        expenseTitleTextView.setText(MessageFormat.format("{0} {1}",
                "Total", view.getResources().getString(R.string.total_expense)));

        expRecyclerAdapter = new ExpenseRecyclerAdapter(
                mainActivityViewModel.getExpenses(),
                this);

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseRecyclerView.setAdapter(expRecyclerAdapter);

        setTotalExpense();

        FloatingActionButton fab = view.findViewById(R.id.fab_expense);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent expenseIntent = new Intent(getActivity(), ExpenseActivity.class);

                startActivityForResult(expenseIntent, ADD_REQUEST_CODE);

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
                    mainActivityViewModel.addExpense(getIntentData(data));
                    setTotalExpense();

                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }

        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == 1) {
                if (data != null) {
                    if (mainActivityViewModel.editExpense(editItemAdapterPosition,
                            getIntentData(data))) {
                        expRecyclerAdapter.notifyItemChanged(editItemAdapterPosition);
                        setTotalExpense();
                    }
                } else {
                    Log.e(TAG, "onActivityResult: intent data is null ");
                }
            }
        }
    }

    private Hashtable<String, String> getIntentData(Intent data) {

        Hashtable<String, String> hashtable = new Hashtable<>();

        hashtable.put("type", data.getStringExtra("type").toLowerCase());
        hashtable.put("date", data.getStringExtra("date"));
        hashtable.put("amount", String.valueOf(data.getIntExtra("amount", 0)));
        hashtable.put("desc", data.getStringExtra("desc"));

        return hashtable;
    }

    private int getSum(List<Expense> obj) {

        int totalSum = 0;

        if (obj.size() == 0) {
            return totalSum;
        }
        for (int i = 0; i < obj.size(); i++) {

            totalSum += obj.get(i).getExpenseAmount();

        }

        return totalSum;
    }

    private void setTotalExpense() {
        int totalExpenses = getSum(mainActivityViewModel.getExpenses());

        totalExpenseTextView.setText(MessageFormat.format("{0} {1}",
                getResources().getString(R.string.rs_symbol),
                String.valueOf(totalExpenses)));
    }

    @Override
    public void onItemClicked(View view, final int position) {
        editItemAdapterPosition = position;
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view, Gravity.CENTER);

        popupMenu.getMenuInflater().inflate(R.menu.options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menu_item:
                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        Expense selectedObj = mainActivityViewModel.getExpenses().get(position);
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

    private void setIntentData(Expense selectedItem, Intent intentData) {
        intentData.putExtra("title", "Edit Expense");
        intentData.putExtra("type", selectedItem.getExpenseType());
        intentData.putExtra("date", selectedItem.getExpenseDate());
        intentData.putExtra("amount", selectedItem.getExpenseAmount());
        intentData.putExtra("desc", selectedItem.getExpenseDesc());
    }

    private void showDialog() {
        DialogFragment dialogFragment = new ConfirmModal();

        dialogFragment.setTargetFragment(ExpensePageFragment.this, 0);
        dialogFragment.show(getParentFragmentManager(), null);

    }

    @Override
    public void onOkClick(DialogFragment dialogFragment) {
        if (mainActivityViewModel.deleteExpense(editItemAdapterPosition)) {
            expRecyclerAdapter.notifyItemRemoved(editItemAdapterPosition);
            setTotalExpense();
            dialogFragment.dismiss();
        }

    }

    @Override
    public void onCancelClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }
}
