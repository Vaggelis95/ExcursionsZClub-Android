package com.zeustech.excursions.ui.navigation.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.SpanConstructor;
import com.zeustech.excursions.models.CartModel;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

class BasketRecyclerAdapter extends ListAdapter<CartModel, BasketRecyclerAdapter.BasketViewHolder> {

    private final Context context;
    private OnDeleteClickListener onDeleteListener;

    private static final DiffUtil.ItemCallback<CartModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartModel model, @NonNull CartModel t1) {
            // compare exc_code
            return model.equals(t1);
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartModel model, @NonNull CartModel t1) {
            return model.getPriceModel().equals(t1.getPriceModel());
        }
    };

    BasketRecyclerAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_basket_cell, viewGroup, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder viewHolder, int i) {
        CartModel model = getItem(i);

        viewHolder.headline.setText(model.getPriceModel().getDescription());

        viewHolder.date.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.date),
                model.getPriceModel().getDate(),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.pickup_point.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.pickup_point),
                model.getPriceModel().getPickUpPoint(),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.pickup_time.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.pickup_time),
                model.getPriceModel().getPickUpTime(),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.adults.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.adults),
                String.valueOf(model.getPriceModel().getAdults()),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.children.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.children),
                String.valueOf(model.getPriceModel().getChildren()),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.infants.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.infants),
                String.valueOf(model.getPriceModel().getInfants()),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.language.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.language),
                model.getPriceModel().getLanguage(),
                ContextCompat.getColor(context, R.color.softRed)));

        viewHolder.price.setText(SpanConstructor.apply(
                context.getResources().getString(R.string.excursion_price),
                String.format(context.getResources().getString(R.string.money_format),
                        model.getPriceModel().getFormattedPrice()),
                ContextCompat.getColor(context, R.color.softRed)));

        Glide.with(context).load(model.getPickPath()).into(viewHolder.image);
    }

    void setOnDeleteListener(OnDeleteClickListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    interface OnDeleteClickListener {
        void onDelete(String exCode);
    }

    class BasketViewHolder extends RecyclerView.ViewHolder {

        private TextView headline, date, pickup_point, pickup_time, adults, children, infants, language, price;
        private ImageView image;

        BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView delete_button = itemView.findViewById(R.id.delete_button);
            image = itemView.findViewById(R.id.image);
            headline = itemView.findViewById(R.id.headline);
            date = itemView.findViewById(R.id.date);
            pickup_point = itemView.findViewById(R.id.pickup_point);
            pickup_time = itemView.findViewById(R.id.pickup_time);
            adults = itemView.findViewById(R.id.adults);
            children = itemView.findViewById(R.id.children);
            infants = itemView.findViewById(R.id.infants);
            language = itemView.findViewById(R.id.language);
            price = itemView.findViewById(R.id.price);
            delete_button.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onDeleteListener != null && position != RecyclerView.NO_POSITION) {
                    onDeleteListener.onDelete(getItem(position).getCode());
                }
            });
            Glide.with(delete_button.getContext())
                    .load(R.drawable.delete_shape)
                    .into(delete_button);
        }

    }

}
