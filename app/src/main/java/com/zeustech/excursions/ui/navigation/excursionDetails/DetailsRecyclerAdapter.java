package com.zeustech.excursions.ui.navigation.excursionDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.models.PicPathModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsRecyclerAdapter extends RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder> {

    private List<PicPathModel> dataSet;
    private Context context;
    private OnImageClickListener listener;

    interface OnImageClickListener {
        void onImageClicked(int position, String pickPath);
    }

    void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    DetailsRecyclerAdapter(Context context, List<PicPathModel> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_excursion_details_cell, viewGroup, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder viewHolder, int i) {
        Glide.with(context).load(dataSet.get(i).getPicPath()).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return (dataSet != null) ? dataSet.size() : 0;
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(v -> {
                if (listener == null) { return; }
                listener.onImageClicked(getAdapterPosition(), dataSet.get(getAdapterPosition()).getPicPath());
            });
        }
    }

}

