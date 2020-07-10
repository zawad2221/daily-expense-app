package info.devram.dainikhatabook.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;

public class DashBoardRecyclerAdapter extends RecyclerView.Adapter<DashBoardRecyclerAdapter.ViewHolder> {

    private static final String TAG = "DashBoardRecyclerAdapte";

    private List dashBoardList;

    public DashBoardRecyclerAdapter(List dashBoardList) {
        this.dashBoardList = dashBoardList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dash_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense;
        Income income;

        Log.i(TAG, "onBindViewHolder: " + getItemCount());

        if (getItemCount() > 0) {
            if (dashBoardList.get(position) instanceof Expense) {
                expense = (Expense) dashBoardList.get(position);
                holder.typeTextView.setText(expense.getExpenseType());
                holder.expenseTextView.setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        expense.getExpenseAmount()));
                holder.incomeTextView.setText(
                        holder.itemView.getContext().getResources().getString(R.string.no_value)
                        );
                holder.expenseTextView.setTextColor(
                        holder.itemView.getContext().getColor(R.color.error)
                );
            }
            if (dashBoardList.get(position) instanceof Income) {
                income = (Income) dashBoardList.get(position);
                holder.typeTextView.setText(income.getIncomeType());
                holder.incomeTextView.setText(MessageFormat.format("{0} {1}",
                                holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                                income.getIncomeAmount()));
                holder.incomeTextView.setTextColor(
                        holder.itemView.getContext().getColor(R.color.alterAccent)
                );
                holder.expenseTextView.setText(
                        holder.itemView.getContext().getResources().getString(R.string.no_value)
                );
            }
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardList.size();
    }

    public void updateData(List updatedList) {
        this.dashBoardList.clear();
        this.dashBoardList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView typeTextView;
        public TextView expenseTextView;
        public TextView incomeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            typeTextView = itemView.findViewById(R.id.dash_type_txt_view);
            expenseTextView = itemView.findViewById(R.id.dash_exp_txt_view);
            incomeTextView = itemView.findViewById(R.id.dash_inc_txt_view);
        }
    }
}
