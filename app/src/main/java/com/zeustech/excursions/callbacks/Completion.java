package com.zeustech.excursions.callbacks;

import androidx.annotation.NonNull;

public interface Completion<T> {
    void onResult(@NonNull T model);
}
