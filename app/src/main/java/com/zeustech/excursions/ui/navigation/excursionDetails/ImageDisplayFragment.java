package com.zeustech.excursions.ui.navigation.excursionDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.tools.ScreenDialogFragment;

public class ImageDisplayFragment extends ScreenDialogFragment {

    private static final String IMAGE_URL = "image_url";

    private String imageUrl;

    public ImageDisplayFragment() {
        // Required empty public constructor
    }

    static ImageDisplayFragment newInstance(String imageUrl) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
        setWidthPercentage(0.80);
        wrapContentHeight(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.image);
        if (imageUrl != null) Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
    }
}
