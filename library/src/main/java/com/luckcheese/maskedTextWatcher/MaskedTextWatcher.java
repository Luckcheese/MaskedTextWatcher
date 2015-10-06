package com.luckcheese.maskedTextWatcher;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class MaskedTextWatcher implements TextWatcher {

    private MaskInfo mask;
    private EditText editText;

    private String originalString;
    private boolean editting;

    public MaskedTextWatcher(String mask, EditText editText) {
        this.editText = editText;
        removeSuggestions();

        setMask(mask);
    }

    private void removeSuggestions() {
        int inputType = editText.getInputType();
        editText.setInputType(inputType | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public void setMask(String mask) {
        if (mask == null || mask.length() == 0) {
            this.mask = null;
        }
        else {
            this.mask = new MaskInfo(mask);

            String currentText = editText.getText().toString();
            if (currentText.length() > 0) {
                editText.setText(applyMaskToString(currentText));
                editText.setSelection(computeCursorNewPos(currentText, editText.getText().toString(), currentText.length(), false));
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mask == null) return;

        if (!editting) {
            editting = true;

            String currentString = s.toString();
            int newStart = start;
            if (before == 1 && count == 0) {
                while (mask.charAt(newStart) != '#') {
                    newStart--;
                }
                currentString = currentString.substring(0, newStart) + currentString.substring(start);
            }

            String withMask = applyMaskToString(currentString);
            editText.setText(withMask);

            int cursorPos = computeCursorNewPos(originalString, withMask, newStart, before > count);
            editText.setSelection(cursorPos);

            editting = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!editting) {
            originalString = s.toString();
        }
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
        String cleanedString = editTextString.replaceAll(mask.fixedCharsRegex, "");
        if (cleanedString.length() > mask.variableCharsQty) {
            return cleanedString.substring(0, mask.variableCharsQty);
        }
        return cleanedString;
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

    protected int computeCursorNewPos(String originalString, String newString, int originalCursorPos, boolean removing) {
        String originalStringSuffixCleaned = clean(originalString.substring(originalCursorPos));
        String newStringCleaned = clean(newString);

        // find original suffix on cleaned newString (check if some chars from original string were removed)
        while (!newStringCleaned.endsWith(originalStringSuffixCleaned) &&
                originalStringSuffixCleaned.length() > 0) {

            int start = removing ? 1 : 0;
            int end = originalStringSuffixCleaned.length();
            if (!removing) {
                end --;
            }
            originalStringSuffixCleaned = originalStringSuffixCleaned.substring(start, end);
        }

        // place cursor before suffix
        int cursorPos = newString.length();
        int suffixPos = originalStringSuffixCleaned.length() - 1;
        for (; cursorPos > originalCursorPos && suffixPos >= 0; cursorPos--) {
            char c = newString.charAt(cursorPos - 1);
            if (c == originalStringSuffixCleaned.charAt(suffixPos)) {
                suffixPos--;
            }
        }

        return cursorPos;
    }

    // ----- Related classes --------------------------------------------------

    private static final class MaskInfo {
        private String mask;

        public String fixedCharsRegex;
        public int variableCharsQty;

        public MaskInfo(String mask) {
            this.mask = mask;

            String fixedChars = mask.replace("#", "");
            variableCharsQty = mask.length() - fixedChars.length();

            HashSet<Character> fixedCharsSet = new HashSet<Character>();
            for (int i = 0; i < fixedChars.length(); i++) {
                fixedCharsSet.add(fixedChars.charAt(i));
            }

            if (fixedCharsSet.size() > 0) {
                fixedCharsRegex = fixedCharsSet.toString().replace(", ", "").replace("-", "\\-");
            }
            else {
                fixedCharsRegex = "";
            }
        }

        public int length() {
            return mask.length();
        }

        public char charAt(int i) {
            return mask.charAt(i);
        }
    }
}
