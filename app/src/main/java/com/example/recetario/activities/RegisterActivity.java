package com.example.recetario.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.recetario.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private static final int PICK_IMAGE = 100;
    ImageView imageViewProfilePicture;
    EditText editTextTextUsername;
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    EditText editTextTextPasswordConfirm;
    CheckBox checkBoxTermsAndConditions;
    Button buttonRegister;
    Button buttonLoadImage;
    Button buttonGetLocation;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    GeoPoint geoPoint;

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

        // Se inicializan las variables de la base de datos y de autenticación
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        // Se asigna la funcionalidad al botón de registro de usuario el metodo debe validar imagen, ubicación, contraseña y correo
        buttonRegister.setOnClickListener(v -> {
            // Se obtiene la imagen
            imageViewProfilePicture.setDrawingCacheEnabled(true);
            imageViewProfilePicture.buildDrawingCache();
            Bitmap image = imageViewProfilePicture.getDrawingCache();
            // Se obtiene el correo
            String email = editTextTextEmailAddress.getText().toString();
            // Se obtiene la contraseña
            String password = editTextTextPassword.getText().toString();
            // Se obtiene la confirmación de la contraseña
            String passwordConfirm = editTextTextPasswordConfirm.getText().toString();
            // Se obtiene el nombre de usuario
            String username = editTextTextUsername.getText().toString();

            // Se valida la información
            if (image == null) {
                Toast.makeText(this, "Por favor, agregue una imagen de perfil", Toast.LENGTH_SHORT).show();
            } else if (geoPoint == null) {
                Toast.makeText(this, "Por favor, proporcione su ubicación.", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Por favor, ingrese un correo válido.", Toast.LENGTH_SHORT).show();
            } else if (!checkBoxTermsAndConditions.isChecked()) {
                Toast.makeText(this, "Por favor, acepte los términos y condiciones.", Toast.LENGTH_SHORT).show();
            } else {
                if (mAuth.getCurrentUser() == null) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Se obtiene el id del usuario
                                    String userId = mAuth.getCurrentUser().getUid();
                                    // Se crea el mapa de datos
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username);
                                    user.put("email", email);
                                    user.put("location", geoPoint);
                                    // Se sube la imagen a Firebase Storage
                                    imageViewProfilePicture.setDrawingCacheEnabled(true);
                                    imageViewProfilePicture.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) imageViewProfilePicture.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();
                                    // Se sube la imagen a Firestorage
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                    StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID() + ".jpg");
                                    user.put("profilePicture", imageRef.getPath());
                                    UploadTask uploadTask = imageRef.putBytes(data);
                                    uploadTask.addOnFailureListener(exception -> {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(this, "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                                    }).addOnSuccessListener(taskSnapshot -> {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                        Toast.makeText(this, "Imagen subida correctamente.", Toast.LENGTH_SHORT).show();
                                    });
                                    // Se sube la información del usuario a Firebase Firestore
                                    db.collection("users").document(userId).set(user)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(this, MainActivity.class);
                                                startActivity(intent);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            });
                }
            }
        });

        // Se asigna la funcionalidad al botón de carga de imagen
        buttonLoadImage.setOnClickListener(v -> {
            Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(iCamera, PICK_IMAGE);
        });

        // Se asigna la funcionalidad al botón de obtener ubicación
        buttonGetLocation.setOnClickListener(v -> {
            // Se verifica que se tenga permiso para acceder a la ubicación
            if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Si no se tiene permiso, se solicita
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                // Si se tiene permiso, se obtiene la ubicación
                getGPS();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageViewProfilePicture.setImageBitmap(bitmap);
            }

        }
    }


    private void getGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                if (geoPoint != null) {
                    Toast.makeText(RegisterActivity.this, "Ubicación obtenida correctamente.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                OnGPS();
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
        });


    }

    private void OnGPS() {
        new AlertDialog.Builder(this)
                .setMessage("El GPS está desactivado, ¿desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}

