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
import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder> {

    //private static final String TAG = "IncomeRecyclerAdapter";

    private List<AccountEntity> incomeList;
    private RecyclerOnClick recyclerOnClick;

    public IncomeRecyclerAdapter(List<AccountEntity> incomeList, RecyclerOnClick recyclerOnClick) {

        this.incomeList = incomeList;
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public IncomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.summary_layout_row,parent,false);

        return new ViewHolder(view,recyclerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        AccountEntity accountEntity = this.incomeList.get(position);
        holder.detailTypeTextView
                .setText(accountEntity.accountType.getType());

        holder.detailAmountTextView
                .setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        String.valueOf(accountEntity.accountMoney.getAmount())));


        switch (accountEntity.accountType.getType().toLowerCase()) {
            case "cash":
                holder.detailImageView.setImageResource(R.drawable.ic_cash_icon);
                break;
            case "salary":
                holder.detailImageView.setImageResource(R.drawable.ic_salary_icon);
                break;
            case "loan":
                holder.detailImageView.setImageResource(R.drawable.ic_loan_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.incomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public ImageView detailImageView;
        public TextView detailTypeTextView;
        public TextView detailAmountTextView;
        private RecyclerOnClick recyclerOnClick;

        public ViewHolder(@NonNull View itemView,RecyclerOnClick recyclerOnClick) {
            super(itemView);

            detailImageView = itemView.findViewById(R.id.detailImgView);
            detailTypeTextView = itemView.findViewById(R.id.detailTypeTextView);
            detailAmountTextView = itemView.findViewById(R.id.detailAmtTxtView);
            this.recyclerOnClick = recyclerOnClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(v,getAdapterPosition());
        }
    }
}
