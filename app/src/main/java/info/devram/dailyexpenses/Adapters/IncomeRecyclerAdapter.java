package info.devram.dailyexpenses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import info.devram.dailyexpenses.Models.Income;
import info.devram.dailyexpenses.R;

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
                .setText(String.valueOf(income.getIncomeAmount()));
    }

    @Override
    public int getItemCount() {
        return this.incomeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView incomeType;
        public TextView incomeAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            incomeType = itemView.findViewById(R.id.inc_dash_txt_view);
            incomeAmount = itemView.findViewById(R.id.inc_amt_dash_txt_view);
        }
    }
}
