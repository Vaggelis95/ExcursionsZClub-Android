package com.zeustech.excursions.ui.home;

import androidx.annotation.Nullable;

public interface HomeFragmentListener {
    void onSuccessfulLogin();
    void onSuccessfulPlaceSelection();
    void onErrorMessage(@Nullable String message);
}
