package com.luckcheese.maskedTextWatcher;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskedTextWatcher implements TextWatcher {

    private String mask;

    public MaskedTextWatcher(String mask) {
        this.mask = mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
