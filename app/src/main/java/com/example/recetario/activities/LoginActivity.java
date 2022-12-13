package com.example.recetario.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.recetario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

// Esta clase es la habilitar el ingreso a la aplicación a los usuarios registrados
public class LoginActivity extends AppCompatActivity {
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    Button loginButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Se obtienen los elementos de la vista
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            if (editTextTextEmailAddress.getText().toString().isEmpty() || editTextTextPassword.getText().toString().isEmpty()) {
                // Si alguno de los campos está vacío, se muestra un mensaje de error
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Por favor, ingrese todos los campos");
                builder.setTitle("Error");
                builder.setPositiveButton("Aceptar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                // Si los campos no están vacíos, se procede a iniciar sesión
                login();
            }
        });

    }

    private void login() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(editTextTextEmailAddress.getText().toString(), editTextTextPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el inicio de sesión es exitoso, se redirige al usuario a la actividad principal
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Inicio de sesión exitoso");
                        builder.setTitle("Éxito");
                        builder.setPositiveButton("Aceptar", (dialog, which) -> {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        // Si el inicio de sesión falla, se muestra un mensaje de error
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("El correo electrónico o la contraseña son incorrectos");
                        builder.setTitle("Error");
                        builder.setPositiveButton("Aceptar", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }
}