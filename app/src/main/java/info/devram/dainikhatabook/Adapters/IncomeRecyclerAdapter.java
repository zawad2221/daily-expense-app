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

import info.devram.dainikhatabook.Models.Income;
import info.devram.dainikhatabook.R;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder> {

    private static final String TAG = "IncomeRecyclerAdapter";

    private List<Income> incomeList;
    private RecyclerOnClick recyclerOnClick;

    public IncomeRecyclerAdapter(List<Income> incomeList, RecyclerOnClick recyclerOnClick) {

        this.incomeList = incomeList;
        this.recyclerOnClick = recyclerOnClick;
    }

    @NonNull
    @Override
    public IncomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.income_row,parent,false);

        return new ViewHolder(view,recyclerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Income income = incomeList.get(position);
        holder.incomeTypeTextView
                .setText(income.getIncomeType());

        holder.incomeAmount
                .setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        String.valueOf(income.getIncomeAmount())));


        switch (income.getIncomeType().toLowerCase()) {
            case "cash":
                holder.incomeImageView.setImageResource(R.drawable.ic_cash_icon);
                break;
            case "salary":
                holder.incomeImageView.setImageResource(R.drawable.ic_salary_icon);
                break;
            case "loan":
                holder.incomeImageView.setImageResource(R.drawable.ic_loan_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.incomeList.size();
    }

    public void updateData(List<Income> newIncomeList) {

        incomeList.clear();
        incomeList.addAll(newIncomeList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public TextView incomeTypeTextView;
        public TextView incomeAmount;
        public ImageView incomeImageView;
        private RecyclerOnClick recyclerOnClick;

        public ViewHolder(@NonNull View itemView,RecyclerOnClick recyclerOnClick) {
            super(itemView);

            incomeTypeTextView = itemView.findViewById(R.id.inc_dash_txt_view);
            incomeAmount = itemView.findViewById(R.id.inc_amt_dash_txt_view);
            incomeImageView = itemView.findViewById(R.id.inc_dash_img_view);
            this.recyclerOnClick = recyclerOnClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(v,getAdapterPosition());
        }
    }
}
