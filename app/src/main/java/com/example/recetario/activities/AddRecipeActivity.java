package com.example.recetario.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.recetario.R;
import com.example.recetario.Utilities.RecetarioDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddRecipeActivity extends AppCompatActivity {

    EditText etRecipeName;
    EditText etRecipeDescription;
    EditText etRecipeIngredients;
    EditText etRecipeInstructions;
    Button btnAddRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        etRecipeName = findViewById(R.id.etRecipeName);
        etRecipeDescription = findViewById(R.id.etRecipeDescription);
        etRecipeIngredients = findViewById(R.id.etRecipeIngredients);
        etRecipeInstructions = findViewById(R.id.etRecipeInstructions);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);

        RecetarioDatabase recetarioDatabase = new RecetarioDatabase(this);

        btnAddRecipe.setOnClickListener(v -> {
            if (checkInputs()) {
                recetarioDatabase.addRecipe(etRecipeName.getText().toString(), etRecipeDescription.getText().toString(), etRecipeIngredients.getText().toString(), etRecipeInstructions.getText().toString());
                finish();
            }
        });

    }

    private boolean checkInputs(){
        if (etRecipeName.getText().toString().isEmpty()) {
            etRecipeName.setError("El nombre de la receta no puede estar vacío");
            return false;
        }
        if (etRecipeDescription.getText().toString().isEmpty()) {
            etRecipeDescription.setError("La descripción de la receta no puede estar vacía");
            return false;
        }
        if (etRecipeIngredients.getText().toString().isEmpty()) {
            etRecipeIngredients.setError("Los ingredientes de la receta no pueden estar vacíos");
            return false;
        }
        if (etRecipeInstructions.getText().toString().isEmpty()) {
            etRecipeInstructions.setError("Las instrucciones de la receta no pueden estar vacías");
            return false;
        }
        return true;
    }

}