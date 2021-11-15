package com.example.mobileauthentication;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    TextInputEditText editTextCountryCode, editTextPhone;
    AppCompatButton buttonContinue;
    Spinner spn_countries;

    String[] countries = { "India", "United States",
            "Indonesia", "France",
            "Italy", "Singapore","New Zealand","Malaysia","Portugal" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);
        spn_countries = findViewById(R.id.spn_countries);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }

                String phoneNumber = code + number;

                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                finish();

            }
        });

        // spinner :

        spn_countries.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                countries);


        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spn_countries.setAdapter(ad);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (countries[i])
        {

            case "India" : editTextCountryCode.setText("+91"); break;
            case "United States" : editTextCountryCode.setText("+1"); break;
            case "Indonesia" : editTextCountryCode.setText("+62"); break;
            case "France" : editTextCountryCode.setText("+33"); break;
            case "Italy" : editTextCountryCode.setText("+39"); break;
            case "Singapore" : editTextCountryCode.setText("+65"); break;
            case "New Zealand" : editTextCountryCode.setText("+64"); break;
            case "Malaysia" : editTextCountryCode.setText("+60"); break;
            case "Portugal" : editTextCountryCode.setText("+351"); break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
