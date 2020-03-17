package com.zeustech.excursions.tools;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class FileReader<T> {

    private T model = null;

    FileReader(@NonNull Context context, int filePath, @NonNull Class<T> mClass) {
        try {
            model = new Gson().fromJson(read(context, filePath), mClass);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public T getModel() {
        return model;
    }

    private String read(Context context, int file) throws IOException {
        String json;
        try (InputStream inputStream = context.getResources().openRawResource(file)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            json = new String(buffer, StandardCharsets.UTF_8);
        }
        return json;
    }

}
