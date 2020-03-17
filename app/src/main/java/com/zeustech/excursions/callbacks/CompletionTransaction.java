package com.zeustech.excursions.callbacks;

import androidx.annotation.Nullable;

public interface CompletionTransaction {
    void onResult(boolean success, @Nullable String message);
}
