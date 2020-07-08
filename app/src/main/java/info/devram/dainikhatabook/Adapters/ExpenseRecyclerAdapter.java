package info.devram.dainikhatabook.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.R;

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder> {

    private static final String TAG = "ExpenseRecyclerAdapter";


    private List<Expense> expenseList;
    private RecyclerOnClick recyclerOnClick;

    public ExpenseRecyclerAdapter(List<Expense> expenseList,RecyclerOnClick recyclerOnClick) {

        this.expenseList = expenseList;
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public ExpenseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row,parent,false);

        return new ViewHolder(view,recyclerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseRecyclerAdapter.ViewHolder holder, int position) {
        //holder.expenseImageView
        Expense expense = expenseList.get(position);
        holder.expenseTypeTextView
                .setText(expense.getExpenseType());
        holder.expenseAmountTextView
                .setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        String.valueOf(expense.getExpenseAmount())));

        switch (expense.getExpenseType().toLowerCase()) {
            case "clothing":
                holder.expenseImageView.setImageResource(R.drawable.ic_cloth_icon);
                break;
            case "entertainment":
                holder.expenseImageView.setImageResource(R.drawable.ic_entertain_icon);
                break;
            case "food":
                holder.expenseImageView.setImageResource(R.drawable.ic_food_icon);
                break;
            case "transport":
                holder.expenseImageView.setImageResource(R.drawable.ic_transport_icon);
                break;
            case "medical":
                holder.expenseImageView.setImageResource(R.drawable.ic_medical_icon);
                break;
            case "shopping":
                holder.expenseImageView.setImageResource(R.drawable.ic_shopping_icon);
                break;
            case "education":
                holder.expenseImageView.setImageResource(R.drawable.ic_education_icon);
                break;
            case "grocery":
                holder.expenseImageView.setImageResource(R.drawable.ic_grocery_icon);
                break;
            case "personal":
                holder.expenseImageView.setImageResource(R.drawable.ic_personal_icon);
                break;
            case "fuel":
                holder.expenseImageView.setImageResource(R.drawable.ic_fuel_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void updateData(List<Expense> newExpenseList) {
        expenseList.clear();
        expenseList.addAll(newExpenseList);
        notifyDataSetChanged();
    }

    public void deleteData(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,expenseList.size());
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView expenseImageView;
        public TextView expenseTypeTextView;
        public TextView expenseAmountTextView;
        private RecyclerOnClick recyclerOnClick;

        public ViewHolder(@NonNull View itemView, RecyclerOnClick recyclerOnClick) {
            super(itemView);

            expenseImageView = itemView.findViewById(R.id.exp_dash_img_view);
            expenseTypeTextView = itemView.findViewById(R.id.exp_dash_txt_view);
            expenseAmountTextView = itemView.findViewById(R.id.exp_amt_dash_txt_view);
            this.recyclerOnClick = recyclerOnClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(v,getAdapterPosition());
        }
    }
}
