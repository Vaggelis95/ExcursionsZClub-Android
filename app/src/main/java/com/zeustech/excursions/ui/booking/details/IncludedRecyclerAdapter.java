package com.zeustech.excursions.ui.booking.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeustech.excursions.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IncludedRecyclerAdapter extends RecyclerView.Adapter<IncludedRecyclerAdapter.ViewHolder> {

    private List<String> dataSet;

    IncludedRecyclerAdapter() {
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.included_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public void setDataSet(List<String> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textField;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textField = itemView.findViewById(R.id.text_field);
        }

        private void bind(String text) {
            textField.setText(text);
        }

    }

}
