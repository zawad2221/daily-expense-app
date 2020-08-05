package info.devram.dainikhatabook.Adapters;

//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.devram.dainikhatabook.Models.DashBoardObject;
import info.devram.dainikhatabook.R;

public class DashBoardRecyclerAdapter extends RecyclerView.Adapter<DashBoardRecyclerAdapter.ViewHolder> {

    private static final String TAG = "DashBoardRecyclerAdapte";

    private List<DashBoardObject> dashBoardList;

    public DashBoardRecyclerAdapter(List<DashBoardObject> dashBoardList) {
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

        DashBoardObject dashBoardObject = dashBoardList.get(position);
        Log.d(TAG, "onBindViewHolder: " + dashBoardObject);
        if (dashBoardList.size() != 0) {
            holder.typeTextView.setText(dashBoardObject.getTypeObject());
            holder.amountTextView.setText(String.valueOf(dashBoardObject.getAmountObject()));
            holder.descTextView.setText(dashBoardObject.getDescObject());
            String myFormat = "dd/MM/YY";
            long dateObject = dashBoardObject.getDateObject();

            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

            holder.dateTextView.setText(sdf.format(dateObject));
            if (dashBoardObject.getIsExpense()) {
                holder.imageView.setImageResource(R.drawable.ic_arrow_up);
            }else {
                holder.imageView.setImageResource(R.drawable.ic_arrow_down);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardList.size();
    }

    public void updateData(List<DashBoardObject> updatedList) {
        this.dashBoardList.clear();
        this.dashBoardList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView typeTextView;
        public TextView amountTextView;
        public TextView descTextView;
        public TextView dateTextView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            typeTextView = itemView.findViewById(R.id.dash_type_text);
            amountTextView = itemView.findViewById(R.id.dashAmountTextView);
            descTextView = itemView.findViewById(R.id.dashDescTextView);
            dateTextView = itemView.findViewById(R.id.dashDateTextView);
            imageView = itemView.findViewById(R.id.dashboardImageView);

        }
    }
}
