package info.devram.dainikhatabook.Adapters;

//import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.R;

public class DashBoardRecyclerAdapter extends RecyclerView.Adapter<DashBoardRecyclerAdapter.ViewHolder> {

    //private static final String TAG = "DashBoardRecyclerAdapte";

    private List<AccountEntity> accounts;
    private RecyclerOnClick recyclerOnClick;

    public DashBoardRecyclerAdapter(List<AccountEntity> accountList,
                                    RecyclerOnClick recyclerOnClick) {
        this.accounts = accountList;
        this.recyclerOnClick = recyclerOnClick;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_recycle_row,parent,false);

        return new ViewHolder(view,recyclerOnClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AccountEntity account = this.accounts.get(position);
        if (this.accounts.size() != 0) {
            holder.typeTextView.setText(account.accountType.getType());
            holder.amountTextView.setText(String.valueOf(account.accountMoney.getAmount()));
            holder.descTextView.setText(account.accountDescription.getDesc());
            String myFormat = "dd/MM/YY";
            long dateObject = account.accountCreatedDate.getCreatedAt();
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
            holder.dateTextView.setText(sdf.format(dateObject));
        }

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void updateData(List<AccountEntity> updatedList) {
        this.accounts.clear();
        this.accounts.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView typeTextView;
        public TextView amountTextView;
        public TextView descTextView;
        public TextView dateTextView;
        private RecyclerOnClick recyclerOnClick;

        public ViewHolder(@NonNull View itemView, RecyclerOnClick recyclerOnClick) {
            super(itemView);

            typeTextView = itemView.findViewById(R.id.detailTypeDataTxtView);
            amountTextView = itemView.findViewById(R.id.detailAmtDataTxtView);
            descTextView = itemView.findViewById(R.id.detailDescDataTxtView);
            dateTextView = itemView.findViewById(R.id.detailDateDataTxtView);
            ImageButton editButton = itemView.findViewById(R.id.detailEditBtn);
            ImageButton deleteButton = itemView.findViewById(R.id.detailDeleteBtn);
            this.recyclerOnClick = recyclerOnClick;
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recyclerOnClick.onItemClicked(v,getAdapterPosition());
        }
    }
}
