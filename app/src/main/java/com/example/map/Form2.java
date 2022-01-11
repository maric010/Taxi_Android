package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class Form2 extends AppCompatActivity {
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_phone = "phone";
    private static final String PREF_code_from_sms = "code_from_sms";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        EditText phone = ((EditText)findViewById(R.id.phone));
        EditText code_from_sms = ((EditText)findViewById(R.id.code_from_sms));

        code_from_sms.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(code_from_sms.getText().toString().length()==5){
                    prefEditor = settings.edit();
                    prefEditor.putString(PREF_phone, phone.getText().toString());
                    prefEditor.putString(PREF_code_from_sms, code_from_sms.getText().toString());
                    prefEditor.apply();
                    my.phone=phone.getText().toString();
                    Intent intent = new Intent(Form2.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        my.phone = settings.getString(PREF_phone,"+998");
        phone.setText(my.phone);
        code_from_sms.setText(settings.getString(PREF_code_from_sms,""));


    }
}