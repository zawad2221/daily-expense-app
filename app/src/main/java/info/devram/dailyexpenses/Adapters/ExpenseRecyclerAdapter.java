package info.devram.dailyexpenses.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.devram.dailyexpenses.Models.Expense;
import info.devram.dailyexpenses.R;

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder> {

    private List<Expense> expenseList;

    public ExpenseRecyclerAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseRecyclerAdapter.ViewHolder holder, int position) {
        //holder.expenseImageView
        Expense expense = expenseList.get(position);
        holder.expenseTypeTextView
                .setText(expense.getExpenseType());
        holder.expenseAmountTextView
                .setText(String.valueOf(expense.getExpenseAmount()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView expenseImageView;
        public TextView expenseTypeTextView;
        public TextView expenseAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseImageView = itemView.findViewById(R.id.exp_dash_img_view);
            expenseTypeTextView = itemView.findViewById(R.id.exp_dash_txt_view);
            expenseAmountTextView = itemView.findViewById(R.id.exp_amt_dash_txt_view);
        }
    }
}
