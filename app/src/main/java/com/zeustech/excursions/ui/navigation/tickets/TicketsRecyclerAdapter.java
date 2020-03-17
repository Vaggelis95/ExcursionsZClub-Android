package com.zeustech.excursions.ui.navigation.tickets;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.models.ExTicketModel;
import com.zeustech.excursions.customViews.DateManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketsRecyclerAdapter extends RecyclerView.Adapter<TicketsRecyclerAdapter.TicketsViewHolder> {

    private List<ExTicketModel> dataSet;
    private OnTicketClickListener listener;

    interface OnTicketClickListener {
        void onTicketClicked(ExTicketModel ticket);
    }

    TicketsRecyclerAdapter() {
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public TicketsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_tickets_cell, viewGroup, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsViewHolder viewHolder, int i) {
        ExTicketModel model = dataSet.get(i);
        Resources res = viewHolder.image.getContext().getResources();
        Glide.with(viewHolder.image.getContext()).load(model.getPicPath()).into(viewHolder.image);
        viewHolder.excDescription.setText(model.getExcDescr());
        viewHolder.pickUpPoint.setText(String.format(res.getString(R.string.pickUpPoint), model.getPickUpPoint()));
        viewHolder.pickUpTime.setText(String.format(res.getString(R.string.pickUpTime), model.getPickUpTime()));

        String date = model.getExcDate();
        if (date != null) {
            String formattedDate = DateManager.convertDate(date, "yyyyMMdd", "dd/MM/yyyy");
            viewHolder.excDate.setText(formattedDate);
        }
        if (model.getStatus() == 2) {
            viewHolder.excDate.setTextColor(res.getColor(R.color.cooks_club_red));
            viewHolder.status.setVisibility(View.VISIBLE);
        } else {
            viewHolder.excDate.setTextColor(res.getColor(R.color.cooks_club_blue));
            viewHolder.status.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (dataSet != null) ? dataSet.size() : 0;
    }

    void setOnExcursionClickListener(OnTicketClickListener listener) {
        this.listener = listener;
    }

    void setDataSet(List<ExTicketModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    class TicketsViewHolder extends RecyclerView.ViewHolder {

        private TextView excDescription, excDate, pickUpPoint, pickUpTime, status;
        private ImageView image;

        TicketsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            excDescription = itemView.findViewById(R.id.excDescription);
            excDate = itemView.findViewById(R.id.excDate);
            pickUpPoint = itemView.findViewById(R.id.pickUpPoint);
            pickUpTime = itemView.findViewById(R.id.pickUpTime);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(v -> {
                if (listener == null) { return; }
                listener.onTicketClicked(dataSet.get(getAdapterPosition()));
            });
        }
    }

}
