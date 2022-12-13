package com.example.recetario.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetario.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {



    ImageView imageViewProfilePicture;
    TextView textViewEmail;
    TextView textViewUsername;
    TextView textViewRegisterDate;
    Button buttonShowLocation;
    Button buttonEditProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Se obtienen los elementos de la vista
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewRegisterDate = findViewById(R.id.textViewRegisterDate);
        buttonShowLocation = findViewById(R.id.buttonShowLocation);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);

        // Se obtienen las instancias de las clases de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Se obtienen los datos del usuario actual
        getUserData();

        buttonEditProfile.setOnClickListener(v -> {
            //Intent intent = new Intent(this, EditProfileActivity.class);
            //startActivity(intent);
        });

        buttonShowLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });



    }

    private void getUserData() {
        // Se obtienen los datos del usuario actual
        String email = mAuth.getCurrentUser().getEmail();
        String username = mAuth.getCurrentUser().getDisplayName();
        Date registerDate = new Date(mAuth.getCurrentUser().getMetadata().getCreationTimestamp());

        // Se muestran los datos en la vista
        textViewEmail.setText(mAuth.getUid());
        textViewUsername.setText(username);
        textViewRegisterDate.setText(registerDate.toString());
        imageViewProfilePicture.setDrawingCacheEnabled(true);
        imageViewProfilePicture.buildDrawingCache();
        StorageReference storageRef = storage.getReference();
        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profilePicture = task.getResult().get("profilePicture").toString();
                StorageReference profilePictureRef = storageRef.child(profilePicture);
                final long ONE_MEGABYTE = 1024 * 1024;
                profilePictureRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewProfilePicture.setImageBitmap(bitmap);
                }).addOnFailureListener(exception -> {
                    Toast.makeText(ProfileActivity.this, "Error al obtener la imagen de perfil", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}