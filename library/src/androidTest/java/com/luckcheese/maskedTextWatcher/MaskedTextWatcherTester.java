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
        keyboardKeyPressed(KeyEvent.KEYCODE_A, "a", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "am", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "a", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_N, "an", 2);

        editText.setSelection(1);
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "amn", 2);

        editText.setSelection(3);
        keyboardKeyPressed(KeyEvent.KEYCODE_M, "amnm", 4);

        editText.setSelection(1, 3);
        keyboardKeyPressed(KeyEvent.KEYCODE_G, "agm", 2);
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
        assertEquals(0, mask.computeCursorNewPos("", "", 0, false));
        assertEquals(1, mask.computeCursorNewPos("", "a", 0, false));
        assertEquals(2, mask.computeCursorNewPos("", "aa", 0, false));
        assertEquals(2, mask.computeCursorNewPos("a", "ab", 1, false));
        assertEquals(1, mask.computeCursorNewPos("b", "ab", 0, false));
        assertEquals(2, mask.computeCursorNewPos("ab", "acb", 1, false));

        mask.setMask("##-##");
        assertEquals(1, mask.computeCursorNewPos("", "a", 0, false));
        assertEquals(3, mask.computeCursorNewPos("a", "ab-", 1, false));
        assertEquals(4, mask.computeCursorNewPos("ab-", "ab-c", 3, false));
        assertEquals(5, mask.computeCursorNewPos("ab-c", "ab-cd", 4, false));

        assertEquals(3, mask.computeCursorNewPos("ab-c", "ad-bc", 1, false));
        assertEquals(4, mask.computeCursorNewPos("ad-bc", "ad-eb", 3, false));
        assertEquals(5, mask.computeCursorNewPos("ad-eb", "ad-ef", 4, false));
    }

    public void testCardNumberMask() throws Exception {
        mask.setMask("#### #### #### ####");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "01", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "012", 3);
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "0123 ", 5);
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "0123 4", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "0123 45", 7);
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "0123 456", 8);
        keyboardKeyPressed(KeyEvent.KEYCODE_7, "0123 4567 ", 10);
        keyboardKeyPressed(KeyEvent.KEYCODE_8, "0123 4567 8", 11);
        keyboardKeyPressed(KeyEvent.KEYCODE_9, "0123 4567 89", 12);
        keyboardKeyPressed(KeyEvent.KEYCODE_A, "0123 4567 89a", 13);
        keyboardKeyPressed(KeyEvent.KEYCODE_B, "0123 4567 89ab ", 15);
        keyboardKeyPressed(KeyEvent.KEYCODE_C, "0123 4567 89ab c", 16);
        keyboardKeyPressed(KeyEvent.KEYCODE_D, "0123 4567 89ab cd", 17);
        keyboardKeyPressed(KeyEvent.KEYCODE_E, "0123 4567 89ab cde", 18);
        keyboardKeyPressed(KeyEvent.KEYCODE_F, "0123 4567 89ab cdef", 19);
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0123 4567 89ab cdef", 19);
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "0123 4567 89ab cdef", 19);
    }

    public void testDateMask() throws Exception {
        mask.setMask("##/##/####");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "01/", 3);
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "01/2", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "01/23/", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "01/23/4", 7);
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "01/23/45", 8);
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "01/23/456", 9);
        keyboardKeyPressed(KeyEvent.KEYCODE_7, "01/23/4567", 10);
    }

    public void testMaskChangingSelection() throws Exception {
        mask.setMask("# #/#-##");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0 ", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "0 1/", 4);
        editText.setSelection(2);
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "0 2/1-", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "0 2/3-1", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "0 2/3-41", 7);
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "0 2/3-45", 8);
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "0 2/3-45", 8);
    }

    public void testPastText() throws Exception {
        mask.setMask("# #/#-##");

        editText.setText("01234");
        String expected = "0 1/2-34";
        assertEquals(expected, editText.getText().toString());
        assertEquals(expected.length(), editText.getSelectionStart());
        assertEquals(expected.length(), editText.getSelectionEnd());
    }

    public void testChangeMask() throws Exception {
        mask.setMask("## ## ##");
        editText.setText("123456");
        assertEquals("12 34 56", editText.getText().toString());
        assertEquals(8, editText.getSelectionStart());

        mask.setMask("### ###");
        assertEquals("123 456", editText.getText().toString());
        assertEquals(7, editText.getSelectionStart());

        mask.setMask("# # # # #");
        assertEquals("1 2 3 4 5", editText.getText().toString());
        assertEquals(9, editText.getSelectionStart());

        mask.setMask("### ###");
        assertEquals("123 45", editText.getText().toString());
        assertEquals(6, editText.getSelectionStart());
    }

    public void testBackspace() throws Exception {
        mask.setMask("# #/#-##");

        editText.setText("01234");
        assertEquals("0 1/2-34", editText.getText().toString());
        assertEquals(8, editText.getSelectionStart());

        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 1/2-3", 7);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 1/2-", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 1/", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 ", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "", 0);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "", 0);

        editText.setText("01234");
        assertEquals("0 1/2-34", editText.getText().toString());
        assertEquals(8, editText.getSelectionStart());

        editText.setSelection(5);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 1/3-4", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0 3/4-", 2);

        editText.setText("");
        mask.setMask("### ###.###-##");
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "0", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_1, "01", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_2, "012 ", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_3, "012 3", 5);
        keyboardKeyPressed(KeyEvent.KEYCODE_4, "012 34", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_5, "012 345.", 8);
        keyboardKeyPressed(KeyEvent.KEYCODE_6, "012 345.6", 9);
        keyboardKeyPressed(KeyEvent.KEYCODE_7, "012 345.67", 10);
        keyboardKeyPressed(KeyEvent.KEYCODE_8, "012 345.678-", 12);
        keyboardKeyPressed(KeyEvent.KEYCODE_9, "012 345.678-9", 13);
        keyboardKeyPressed(KeyEvent.KEYCODE_0, "012 345.678-90", 14);
        assertEquals(14, editText.getSelectionStart());

        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 345.678-9", 13);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 345.678-", 12);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 345.67", 10);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 345.6", 9);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 345.", 8);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 34", 6);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 3", 5);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "012 ", 4);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "01", 2);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "0", 1);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "", 0);
        keyboardKeyPressed(KeyEvent.KEYCODE_DEL, "", 0);
    }

    // ----- Helper methods ---------------------------------------------------

    private void keyboardKeyPressed(int keyCode, String expectedResult, int expectedCursorPor) {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        assertEquals(expectedResult, editText.getText().toString());
        assertEquals(expectedCursorPor, editText.getSelectionStart());
    }
}
