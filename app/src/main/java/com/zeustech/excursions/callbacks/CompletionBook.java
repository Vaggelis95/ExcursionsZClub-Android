package com.zeustech.excursions.callbacks;

import com.zeustech.excursions.models.ExDaysModel;
import com.zeustech.excursions.models.ExLanguageModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface CompletionBook {
    void onSuccess(@NonNull ExDaysModel days, @NonNull ExLanguageModel[] languages);
    void onFailure(@Nullable String description);
}
