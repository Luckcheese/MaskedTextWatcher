package com.luckcheese.maskedTextWatcher;

import android.test.AndroidTestCase;
import android.view.KeyEvent;
import android.widget.EditText;

public class MaskedTextWatcherTester extends AndroidTestCase {

    private EditText editText;
    private MaskedTextWatcher watcher;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        watcher = new MaskedTextWatcher(null);

        editText = new EditText(getContext());
        editText.addTextChangedListener(watcher);
        editText.requestFocus();
    }

    public void testNullMask() throws Exception {
        keyboardKeyPressed(KeyEvent.KEYCODE_A, "a");
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "am");
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "a");
        keyboardKeyPressed(KeyEvent.KEYCODE_N, "an");

        editText.setSelection(1);
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "amn");

        editText.setSelection(3);
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "amnm");

        editText.setSelection(1, 3);
        keyboardKeyPressed(KeyEvent.KEYCODE_G, "agm");
    }

    // ----- Helper methods ---------------------------------------------------

    private void keyboardKeyPressed(int keyCode, String expectedResult) {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        assertEquals(expectedResult, editText.getText().toString());
    }
}
