package com.example.whatthechef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class Main2Activity extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CountryCodePicker ccp;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

    }
}
