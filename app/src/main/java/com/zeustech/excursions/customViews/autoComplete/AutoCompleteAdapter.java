package com.zeustech.excursions.customViews.autoComplete;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.CustomTypefaceSpan;
import com.zeustech.excursions.customViews.FontCache;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AutoCompleteAdapter<T extends AutoCompleteData> extends ArrayAdapter<T> implements AdapterView.OnItemClickListener {

    public interface OnItemClickListener<T> {
        void onItemClick(@Nullable T model);
    }

    private List<T> dataSet;
    private String filterPattern;

    private final int resource;
    private OnItemClickListener<T> onItemClickListener;

    public AutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, 0);
        this.resource = resource;
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        SpannableString str = null;
        T model = getItem(position);
        if (model != null && filterPattern != null) {
            String description = model.getText();
            String comparableText = description.toLowerCase().trim();
            int index = comparableText.indexOf(filterPattern);
            if (index != -1) {
                str = new SpannableString(description);
                str.setSpan(new CustomTypefaceSpan(FontCache.getTypeface(parent.getContext(), R.font.brandon_bld)), index, index + filterPattern.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        onBindView(getItem(position), str, convertView);
        return convertView;
    }

    public abstract void onBindView(@Nullable T model, @Nullable SpannableString text, @NonNull View viewHolder);

    @NonNull
    public List<T> getDataSet() {
        return dataSet;
    }

    public void setDataSet(@NonNull List<T> dataSet) {
        this.dataSet = dataSet;
        if (this.dataSet.equals(dataSet)) return;
        notifyAdapter(dataSet);
    }

    private void notifyAdapter(@NonNull List<T> dataSet) {
        clear();
        addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getItem(position));
        }
    }

    public void setOnItemClickListener(@NonNull AutoCompleteTextView tv, @NonNull OnItemClickListener<T> onItemClickListener) {
        tv.setOnItemClickListener(this);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    private List<T> getSuggestions(@NonNull String filterPattern) {
        List<T> list = new ArrayList<>();
        for (T model : dataSet) {
            if (!model.getText().toLowerCase().contains(filterPattern.toLowerCase().trim())) continue;
            list.add(model);
        }
        return list;
    }

    @Nullable
    public T getFirstSuggestion(@NonNull String filterPattern) {
        List<T> suggestions = getSuggestions(filterPattern);
        return suggestions.size() > 0 ? suggestions.get(0) : null;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter () {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                List<T> suggestions = new ArrayList<>();
                if (charSequence != null && dataSet.size() != 0) {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    AutoCompleteAdapter.this.filterPattern = filterPattern;
                    suggestions = getSuggestions(filterPattern);
                }
                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyAdapter(filterResults.values != null ? (List<T>) filterResults.values : new ArrayList<>());
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((T) resultValue).getText();
            }
        };
    }

}
