package com.zeustech.excursions.http.requests;

import androidx.annotation.NonNull;

public class ExcursionRequest {

    private final String excCode, language;

    public ExcursionRequest(String excCode, String language) {
        this.excCode = excCode;
        this.language = language;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExcursionRequest{" +
                "excCode='" + excCode + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

}
