package com.zeustech.excursions.callbacks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface CompletionHandler<T> {
    void onSuccess(@NonNull T model);
    void onFailure(@Nullable String description);
}
