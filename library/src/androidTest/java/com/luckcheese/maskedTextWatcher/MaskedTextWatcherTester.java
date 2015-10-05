package com.luckcheese.maskedTextWatcher;

import android.test.AndroidTestCase;
import android.view.KeyEvent;
import android.widget.EditText;

public class MaskedTextWatcherTester extends AndroidTestCase {

    private EditText editText;
    private MaskedTextWatcher mask;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mask = new MaskedTextWatcher(null);

        editText = new EditText(getContext());
        editText.addTextChangedListener(mask);
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

    public void testCleanUpString() throws Exception {
        mask.setMask("##");
        assertEquals("a", mask.clean("a"));
        assertEquals("aa", mask.clean("aa"));
        assertEquals("ab", mask.clean("ab"));

        mask.setMask("# #");
        assertEquals("a", mask.clean("a"));
        assertEquals("aa", mask.clean("a a"));
        assertEquals("ab", mask.clean("a b"));
        assertEquals("ac", mask.clean("acb"));
        assertEquals("ac", mask.clean("ac "));

        mask.setMask(" # #");
        assertEquals("a", mask.clean(" a"));
        assertEquals("aa", mask.clean(" a a"));
        assertEquals("ab", mask.clean(" a b"));
        assertEquals("#b", mask.clean(" # b"));
    }

    public void testFormat() throws Exception {
        mask.setMask("##");
        assertEquals("a", mask.format("a"));
        assertEquals("aa", mask.format("aa"));
        assertEquals("ab", mask.format("ab"));

        mask.setMask("# #");
        assertEquals("a ", mask.format("a"));
        assertEquals("a a", mask.format("aa"));
        assertEquals("a b", mask.format("ab"));

        mask.setMask(" #-#");
        assertEquals(" a-", mask.format("a"));
        assertEquals(" a-a", mask.format("aa"));
        assertEquals(" a-b", mask.format("ab"));

        mask.setMask("# #/#-##00");
        assertEquals("0 ", mask.format("0"));
        assertEquals("0 1/", mask.format("01"));
        assertEquals("0 2/1-", mask.format("021"));
        assertEquals("0 2/1-3", mask.format("0213"));
        assertEquals("0 2/1-3400", mask.format("02134"));
    }

    // ----- Helper methods ---------------------------------------------------

    private void keyboardKeyPressed(int keyCode, String expectedResult) {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        assertEquals(expectedResult, editText.getText().toString());
    }
}
