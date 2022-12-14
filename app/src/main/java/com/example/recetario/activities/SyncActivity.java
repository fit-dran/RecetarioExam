package com.example.recetario.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.recetario.R;
import com.example.recetario.Utilities.RecetarioDatabase;
import com.example.recetario.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncActivity extends AppCompatActivity {

    TextView textViewRecipeQuantity;
    Button buttonSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        textViewRecipeQuantity = findViewById(R.id.textViewRecipeQuantity);
        buttonSync = findViewById(R.id.buttonSync);

        RecetarioDatabase recetarioDatabase = new RecetarioDatabase(this);
        ArrayList<Recipe> recipes = recetarioDatabase.getAllRecipes();
        textViewRecipeQuantity.setText("Recetas: " + recipes.size());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        buttonSync.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sincronización");
            builder.setMessage("¿Desea sincronizar las recetas?");
            builder.setPositiveButton("Sí", (dialog, which) -> {
                for (Recipe recipe : recipes) {
                    Map<String, Object> recipeMap = new HashMap<>();
                    recipeMap.put("name", recipe.getName());
                    recipeMap.put("description", recipe.getDescription());
                    recipeMap.put("ingredients", recipe.getIngredients());
                    recipeMap.put("preparation", recipe.getInstructions());
                    recipeMap.put("user", mAuth.getCurrentUser().getUid());
                    db.collection("recipes").document().set(recipeMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            builder.setTitle("Sincronización");
                            builder.setMessage("Recetas sincronizadas correctamente");
                            builder.setPositiveButton("Aceptar", (dialog1, which1) -> {
                                recetarioDatabase.deleteAllRecipes();
                                finish();
                            });
                            builder.show();
                        } else {
                            builder.setTitle("Sincronización");
                            builder.setMessage("Error al sincronizar las recetas");
                            builder.setPositiveButton("Aceptar", (dialog12, which12) -> {
                            });
                            builder.show();
                        }
                    });

                }
                builder.setTitle("Sincronización");
                builder.setMessage("Recetas sincronizadas");
                builder.setPositiveButton("Aceptar", (dialog1, which1) -> {
                    finish();
                });
                builder.show();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
            });
            builder.show();

        });


    }
}