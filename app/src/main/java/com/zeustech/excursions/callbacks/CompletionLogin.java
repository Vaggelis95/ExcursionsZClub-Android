package com.zeustech.excursions.callbacks;

import com.zeustech.excursions.models.ExLoginModel;
import com.zeustech.excursions.models.ExcursionsModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface CompletionLogin {
    void onSuccess(@NonNull ExLoginModel login, @NonNull List<ExcursionsModel> excursions);
    void onFailure(@Nullable String description);
}
