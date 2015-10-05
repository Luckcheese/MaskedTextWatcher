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

        editText = new EditText(getContext());
        mask = new MaskedTextWatcher(null, editText);

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

        mask.setMask("# #/#-##");
        assertEquals("0", mask.clean("0"));
        assertEquals("0", mask.clean("0 "));
        assertEquals("01", mask.clean("0 1"));
        assertEquals("021", mask.clean("0 21"));
        assertEquals("021", mask.clean("0 2/1"));
        assertEquals("021", mask.clean("0 2/1-"));
        assertEquals("021", mask.clean("02 1/"));
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

    public void testCursorPos() throws Exception {
        mask.setMask("###");
        assertEquals(0, mask.computeCursorNewPos("", "", 0));
        assertEquals(1, mask.computeCursorNewPos("", "a", 0));
        assertEquals(2, mask.computeCursorNewPos("", "aa", 0));
        assertEquals(2, mask.computeCursorNewPos("a", "ab", 1));
        assertEquals(1, mask.computeCursorNewPos("b", "ab", 0));
        assertEquals(2, mask.computeCursorNewPos("ab", "acb", 1));

        mask.setMask("##-##");
        assertEquals(1, mask.computeCursorNewPos("", "a", 0));
        assertEquals(3, mask.computeCursorNewPos("a", "ab-", 1));
        assertEquals(4, mask.computeCursorNewPos("ab-", "ab-c", 3));
        assertEquals(5, mask.computeCursorNewPos("ab-c", "ab-cd", 4));

        assertEquals(3, mask.computeCursorNewPos("ab-c", "ad-bc", 1));
        assertEquals(4, mask.computeCursorNewPos("ad-bc", "ad-eb", 3));
        assertEquals(5, mask.computeCursorNewPos("ad-eb", "ad-ef", 4));
    }

    public void testCardNumberMask() throws Exception {
        mask.setMask("#### #### #### ####");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0");
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "01");
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "012");
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "0123 ");
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "0123 4");
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "0123 45");
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "0123 456");
        keyboardKeyPressed(KeyEvent.KEYCODE_7, "0123 4567 ");
        keyboardKeyPressed(KeyEvent.KEYCODE_8, "0123 4567 8");
        keyboardKeyPressed(KeyEvent.KEYCODE_9, "0123 4567 89");
        keyboardKeyPressed(KeyEvent.KEYCODE_A, "0123 4567 89a");
        keyboardKeyPressed(KeyEvent.KEYCODE_B, "0123 4567 89ab ");
        keyboardKeyPressed(KeyEvent.KEYCODE_C, "0123 4567 89ab c");
        keyboardKeyPressed(KeyEvent.KEYCODE_D, "0123 4567 89ab cd");
        keyboardKeyPressed(KeyEvent.KEYCODE_E, "0123 4567 89ab cde");
        keyboardKeyPressed(KeyEvent.KEYCODE_F, "0123 4567 89ab cdef");
    }

    public void testDateMask() throws Exception {
        mask.setMask("##/##/####");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0");
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "01/");
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "01/2");
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "01/23/");
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "01/23/4");
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "01/23/45");
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "01/23/456");
        keyboardKeyPressed(KeyEvent.KEYCODE_7, "01/23/4567");
    }

    public void testMaskChangingSelection() throws Exception {
        mask.setMask("# #/#-##");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0 ");
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "0 1/");
        editText.setSelection(2);
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "0 2/1-");
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "0 2/3-1");
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "0 2/3-41");
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "0 2/3-45");
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "0 2/3-45");
    }

    // ----- Helper methods ---------------------------------------------------

    private void keyboardKeyPressed(int keyCode, String expectedResult) {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        assertEquals(expectedResult, editText.getText().toString());
    }
}
