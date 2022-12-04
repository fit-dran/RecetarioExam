package com.example.recetario.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.recetario.R;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageViewProfilePicture;
    EditText editTextTextUsername;
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    EditText editTextTextPasswordConfirm;
    CheckBox checkBoxTermsAndConditions;
    Button buttonRegister;
    Button buttonLoadImage;
    Button buttonGetLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Se obtienen los elementos de la vista
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);
        editTextTextUsername = findViewById(R.id.editTextTextUsername);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextPasswordConfirm = findViewById(R.id.editTextTextPasswordConfirm);
        checkBoxTermsAndConditions = findViewById(R.id.checkBoxTermsAndConditions);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLoadImage = findViewById(R.id.buttonLoadImage);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);

        //




    }
}