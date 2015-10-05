package com.luckcheese.maskedTextWatcher;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskedTextWatcher implements TextWatcher {

    private String mask;
    private int variableCharsQty;

    public MaskedTextWatcher(String mask) {
        this.mask = mask;
        setMask(mask);
    }

    public void setMask(String mask) {
        if (mask == null || mask.length() == 0) {
            this.mask = null;
        }
        else {
            this.mask = mask;
            this.variableCharsQty = 0;
            for (int i = 0; i < mask.length(); i++) {
                if (mask.charAt(i) == '#') {
                    variableCharsQty++;
                }
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mask == null) return;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        // do nothing
    }

    protected String applyMaskToString(String originalString) {
        if (mask == null) return originalString;
        return format(clean(originalString));
    }

    protected String clean(String editTextString) {
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < editTextString.length(); i++) {
            char c = editTextString.charAt(i);
            if (c != mask.charAt(i) || c == '#') {
                cleaned.append(c);
            }

            if (cleaned.length() == variableCharsQty) {
                break;
            }
        }
        return cleaned.toString();
    }

    protected String format(String cleanedString) {
        StringBuilder formatted = new StringBuilder();
        for (int maskIndex = 0, stringIndex = 0; maskIndex < mask.length(); maskIndex++) {
            char c = mask.charAt(maskIndex);
            if (c == '#') {
                if (stringIndex >= cleanedString.length()) break;
                formatted.append(cleanedString.charAt(stringIndex));
                stringIndex++;
            }
            else {
                formatted.append(c);
            }
        }
        return formatted.toString();
    }
}
