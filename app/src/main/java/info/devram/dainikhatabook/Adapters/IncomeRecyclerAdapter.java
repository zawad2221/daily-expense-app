package info.devram.dainikhatabook.Adapters;

import android.content.Context;
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

    private List<Income> incomeList;
    private Context context;

    public IncomeRecyclerAdapter(Context context,List<Income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.income_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Income income = incomeList.get(position);
        holder.incomeType
                .setText(income.getIncomeType());

        holder.incomeAmount
                .setText(MessageFormat.format("{0} {1}",
                        holder.itemView.getContext().getResources().getString(R.string.rs_symbol),
                        String.valueOf(income.getIncomeAmount())));
        switch (income.getIncomeType()) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView incomeType;
        public TextView incomeAmount;
        public ImageView incomeImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            incomeType = itemView.findViewById(R.id.inc_dash_txt_view);
            incomeAmount = itemView.findViewById(R.id.inc_amt_dash_txt_view);
            incomeImageView = itemView.findViewById(R.id.inc_dash_img_view);
        }
    }
}
