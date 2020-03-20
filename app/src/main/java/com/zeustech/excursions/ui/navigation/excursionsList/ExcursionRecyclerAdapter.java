package com.zeustech.excursions.ui.navigation.excursionsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.models.ExcursionsModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ExcursionRecyclerAdapter extends ListAdapter<ExcursionsModel, ExcursionRecyclerAdapter.ViewHolder> {

    interface OnExcursionClickListener {
        void onExcursionClicked(String exc_code, String exc_name);
    }

    private final static DiffUtil.ItemCallback<ExcursionsModel> callback = new DiffUtil.ItemCallback<ExcursionsModel>() {

        @Override
        public boolean areItemsTheSame(@NonNull ExcursionsModel model, @NonNull ExcursionsModel t1) {
            return model.areItemsTheSame(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExcursionsModel model, @NonNull ExcursionsModel t1) {
            return model.areContentsTheSame(t1);
        }

    };

    private OnExcursionClickListener listener;
    private boolean isUserInteractionEnabled;

    ExcursionRecyclerAdapter() {
        super(callback);
        isUserInteractionEnabled = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_excursions_cell, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExcursionsModel excursion = getItem(position);
        if (excursion != null) holder.bind(excursion);
    }

    void setOnExcursionClickListener(OnExcursionClickListener listener) {
        this.listener = listener;
    }

    void setUserInteractionEnabled(boolean enabled) {
        isUserInteractionEnabled = enabled;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image, noImage;
        private TextView headline, noImageDescription;
        private ProgressBar progressBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            noImage = itemView.findViewById(R.id.no_image);
            headline = itemView.findViewById(R.id.headline);
            progressBar = itemView.findViewById(R.id.progress_bar);
            noImageDescription = itemView.findViewById(R.id.no_image_description);

            itemView.setOnClickListener(v -> {
                if (isUserInteractionEnabled) {
                    if (listener == null) return;
                    ExcursionsModel excursion = getItem(getAdapterPosition());
                    if (excursion != null) {
                        listener.onExcursionClicked(excursion.getCode(), excursion.getDescription());
                    }
                }
            });
        }

        private void bind(@NonNull ExcursionsModel excursion) {
            boolean imageAvailable = excursion.isImageAvailable();
            headline.setText(excursion.getDescription());
            progressBar.setVisibility(imageAvailable ? View.VISIBLE : View.INVISIBLE);
            noImage.setVisibility(imageAvailable ? View.INVISIBLE : View.VISIBLE);
            noImageDescription.setVisibility(imageAvailable ? View.INVISIBLE : View.VISIBLE);
            image.setVisibility(imageAvailable ? View.VISIBLE : View.INVISIBLE);

            if (imageAvailable) {
                Glide.with(noImage.getContext()).clear(noImage);
                Glide.with(image.getContext())
                        .load(excursion.getPicPath())
                        .centerCrop()
                        .into(image);
            } else {
                Glide.with(image.getContext()).clear(image);
                Glide.with(noImage.getContext())
                        .load(R.drawable.image_not_available)
                        .into(noImage);
            }

        }

    }

}
