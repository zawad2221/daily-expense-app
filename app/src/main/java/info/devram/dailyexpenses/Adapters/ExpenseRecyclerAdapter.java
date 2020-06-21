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
                .setText(String.valueOf(expense.getExpenseAmount()));
        switch (expense.getExpenseType()) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView expenseImageView;
        public TextView expenseTypeTextView;
        public TextView expenseAmountTextView;
        private RecyclerOnClick recyclerOnClick;

        public ViewHolder(@NonNull View itemView,RecyclerOnClick recyclerOnClick) {
            super(itemView);

            expenseImageView = itemView.findViewById(R.id.exp_dash_img_view);
            expenseTypeTextView = itemView.findViewById(R.id.exp_dash_txt_view);
            expenseAmountTextView = itemView.findViewById(R.id.exp_amt_dash_txt_view);
            this.recyclerOnClick = recyclerOnClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(getAdapterPosition());
        }
    }
}
