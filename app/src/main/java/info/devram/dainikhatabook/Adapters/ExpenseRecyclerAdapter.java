package info.devram.dainikhatabook.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Models.Expense;
import info.devram.dainikhatabook.R;

public class ExpenseRecyclerAdapter extends
        RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder> {

    //private static final String TAG = "ExpenseRecyclerAdapter";


    private List<AccountEntity> expenseList;
    private RecyclerOnClick recyclerOnClick;

    public ExpenseRecyclerAdapter(List<AccountEntity> expenseList,RecyclerOnClick recyclerOnClick) {

        this.expenseList = expenseList;
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public ExpenseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summary_layout_row,parent,false);

        return new ViewHolder(view,recyclerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseRecyclerAdapter.ViewHolder holder, int position) {
        AccountEntity accountEntity = this.expenseList.get(position);
        holder.detailTypeTextView
                .setText(accountEntity.accountType.getType());
        holder.detailAmountTextView
                .setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        String.valueOf(accountEntity.accountMoney.getAmount())));

        switch (accountEntity.accountType.getType().toLowerCase()) {
            case "clothing":
                holder.detailImageView.setImageResource(R.drawable.ic_cloth_icon);
                break;
            case "entertainment":
                holder.detailImageView.setImageResource(R.drawable.ic_entertain_icon);
                break;
            case "food":
                holder.detailImageView.setImageResource(R.drawable.ic_food_icon);
                break;
            case "transport":
                holder.detailImageView.setImageResource(R.drawable.ic_transport_icon);
                break;
            case "medical":
                holder.detailImageView.setImageResource(R.drawable.ic_medical_icon);
                break;
            case "shopping":
                holder.detailImageView.setImageResource(R.drawable.ic_shopping_icon);
                break;
            case "education":
                holder.detailImageView.setImageResource(R.drawable.ic_education_icon);
                break;
            case "grocery":
                holder.detailImageView.setImageResource(R.drawable.ic_grocery_icon);
                break;
            case "personal":
                holder.detailImageView.setImageResource(R.drawable.ic_personal_icon);
                break;
            case "fuel":
                holder.detailImageView.setImageResource(R.drawable.ic_fuel_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

//    public void onAddItem(List<Expense> expenses) {
//        this.expenseList.clear();
//        this.expenseList.addAll(expenses);
//        notifyDataSetChanged();
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView detailImageView;
        public TextView detailTypeTextView;
        public TextView detailAmountTextView;
        private RecyclerOnClick recyclerOnClick;
        public TextView seeMoreTextView;

        public ViewHolder(@NonNull View itemView, RecyclerOnClick recyclerOnClick) {
            super(itemView);

            detailImageView = itemView.findViewById(R.id.detailImgView);
            detailTypeTextView = itemView.findViewById(R.id.detailTypeTextView);
            detailAmountTextView = itemView.findViewById(R.id.detailAmtTxtView);
            seeMoreTextView = itemView.findViewById(R.id.detailMoreTextView);
            this.recyclerOnClick = recyclerOnClick;
            seeMoreTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(v,getLayoutPosition());
        }
    }
}
