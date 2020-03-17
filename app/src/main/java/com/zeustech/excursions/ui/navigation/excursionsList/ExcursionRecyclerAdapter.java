package com.zeustech.excursions.ui.navigation.excursionsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.models.ExcursionsModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ExcursionRecyclerAdapter extends RecyclerView.Adapter<ExcursionRecyclerAdapter.ExcursionViewHolder> {

    private List<ExcursionsModel> dataSet;
    private OnExcursionClickListener listener;
    private boolean isUserInteractionEnabled;

    interface OnExcursionClickListener {
        void onExcursionClicked(String exc_code, String exc_name);
    }

    ExcursionRecyclerAdapter() {
        this.dataSet = new ArrayList<>();
        isUserInteractionEnabled = true;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_excursions_cell, viewGroup, false);
        return new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder viewHolder, int position) {
        ExcursionsModel excursion = dataSet.get(position);
        if (excursion != null) viewHolder.bind(excursion);
    }

    @Override
    public int getItemCount() {
        return (dataSet != null) ? dataSet.size() : 0;
    }

    void setDataSet(List<ExcursionsModel> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    void setOnExcursionClickListener(OnExcursionClickListener listener) {
        this.listener = listener;
    }

    void setUserInteractionEnabled(boolean enabled) {
        isUserInteractionEnabled = enabled;
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout noImageContainer, cardView;

        private ImageView image, noImage;
        private TextView headline;

        ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            noImageContainer = itemView.findViewById(R.id.no_image_container);
            noImage = itemView.findViewById(R.id.no_image);

            cardView = itemView.findViewById(R.id.card_view);
            image = itemView.findViewById(R.id.image);
            headline = itemView.findViewById(R.id.headline);

            itemView.setOnClickListener(v -> {
                if (isUserInteractionEnabled) {
                    if (listener == null) return;
                    listener.onExcursionClicked(dataSet.get(getAdapterPosition()).getCode(),
                            dataSet.get(getAdapterPosition()).getDescription());
                }
            });
        }

        private void bind(@NonNull ExcursionsModel excursion) {
            boolean imageAvailable = excursion.isImageAvailable();
            headline.setText(excursion.getDescription());
            cardView.setVisibility(imageAvailable ? View.VISIBLE : View.INVISIBLE);
            noImageContainer.setVisibility(imageAvailable ? View.INVISIBLE : View.VISIBLE);
            if (imageAvailable) {
                Glide.with(image.getContext()).load(excursion.getPicPath()).into(image);
            } else {
                Glide.with(image.getContext()).load(R.drawable.image_not_available).into(noImage);
            }
        }

    }

}
