package com.luckcheese.maskedTextWatcherSample;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.luckcheese.maskedTextWatcher.MaskedTextWatcher;


public class SampleActivity extends Activity implements View.OnClickListener {

    private EditText maskField;
    private EditText testField;
    private RadioGroup radioGroup;

    private MaskedTextWatcher maskedTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        maskField = (EditText) findViewById(R.id.mask);
        radioGroup = (RadioGroup) findViewById(R.id.keyboardStyle);
        testField = (EditText) findViewById(R.id.testArea);

        findViewById(R.id.apply).setOnClickListener(this);

        maskedTextWatcher = new MaskedTextWatcher(null, testField);
        testField.addTextChangedListener(maskedTextWatcher);
    }

    // ----- View.OnClickListener ---------------------------------------------

    @Override
    public void onClick(View v) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.numbers:
                testField.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case R.id.text:
                testField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
        maskedTextWatcher.setMask(maskField.getText().toString());
    }
}
