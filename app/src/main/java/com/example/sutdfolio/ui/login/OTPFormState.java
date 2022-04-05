package com.example.sutdfolio.ui.login;

import androidx.annotation.Nullable;

public class OTPFormState {
    @Nullable
    private Integer digitsError;
    private boolean isDataValid;

    OTPFormState(@Nullable Integer digitsError) {
        this.digitsError = digitsError;
        this.isDataValid = false;
    }

    OTPFormState(boolean isDataValid) {
        this.digitsError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getdigitsError() {
        return digitsError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
