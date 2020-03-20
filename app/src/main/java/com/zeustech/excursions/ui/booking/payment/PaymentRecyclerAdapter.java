package com.zeustech.excursions.ui.booking.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.SpanConstructor;
import com.zeustech.excursions.models.ExPriceModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentRecyclerAdapter extends RecyclerView.Adapter<PaymentRecyclerAdapter.PaymentViewHolder> {

    private ArrayList<ExPriceModel> dataSet;
    private Context context;

    PaymentRecyclerAdapter(@NonNull Context context) {
        this.context = context;
        this.dataSet = new ArrayList<>();
    }

    void setDataSet(ArrayList<ExPriceModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_payment_cell, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder viewHolder, int i) {
        ExPriceModel model = dataSet.get(i);
        viewHolder.exc_name.setText(model.getDescription());

        viewHolder.pick_up_point.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.pickup_point),
                model.getPickUpPoint(),
                ContextCompat.getColor(context, R.color.soft_red)));

        viewHolder.pick_up_time.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.pickup_time),
                model.getPickUpTime(),
                ContextCompat.getColor(context, R.color.soft_red)));

        viewHolder.price.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.excursion_price),
                String.format(context.getResources().getString(R.string.money_format), model.getFormattedPrice()),
                ContextCompat.getColor(context, R.color.soft_red)));
    }

    @Override
    public int getItemCount() {
        return (dataSet != null) ? dataSet.size() : 0;
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {

        private TextView exc_name, pick_up_point, pick_up_time, price;

        PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            exc_name = itemView.findViewById(R.id.exc_name);
            pick_up_point = itemView.findViewById(R.id.pick_up_point);
            pick_up_time = itemView.findViewById(R.id.pick_up_time);
            price = itemView.findViewById(R.id.price);
        }
    }

}
