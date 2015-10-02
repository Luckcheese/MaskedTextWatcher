package com.luckcheese.maskedTextWatcherSample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.luckcheese.maskedTextWatcher.MaskedTextWatcher;


public class SampleActivity extends Activity implements View.OnClickListener {

    private EditText maskField;
    private EditText testField;

    private MaskedTextWatcher maskedTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        maskField = (EditText) findViewById(R.id.mask);
        testField = (EditText) findViewById(R.id.testArea);

        findViewById(R.id.apply).setOnClickListener(this);

        maskedTextWatcher = new MaskedTextWatcher(null);
        testField.addTextChangedListener(maskedTextWatcher);
    }

    // ----- View.OnClickListener ---------------------------------------------

    @Override
    public void onClick(View v) {
        maskedTextWatcher.setMask(maskField.getText().toString());
    }
}
