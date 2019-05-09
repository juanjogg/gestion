package com.example.healthycards;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CrearActividad extends AppCompatActivity {

    private EditText nombreAct, etDescripcion, etTiempo;
    private Button btnEnviar, btnLoadImage;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private Spinner comboDificultad;
    private StorageReference storageReference;
    private Uri imgUri;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent toLogin = new Intent(CrearActividad.this, MainActivity.class);
            Toast.makeText(CrearActividad.this, "Debe autenticarse", Toast.LENGTH_SHORT);
            startActivity(toLogin);
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_actividad);

        imgUri = null;
        comboDificultad = findViewById(R.id.spinDifiActividad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combo_dificultades, android.R.layout.simple_spinner_item);
        comboDificultad.setAdapter(adapter);

        btnLoadImage = findViewById(R.id.btnImagenActividad);
        nombreAct = findViewById(R.id.txtnombreActividad);
        etDescripcion = findViewById(R.id.txtdescActividad);


        etTiempo = findViewById(R.id.txttiempoActividad);
        btnEnviar = findViewById(R.id.btnCrearActividad);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Activity");
        storageReference = FirebaseStorage.getInstance().getReference();
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgUri == null){
                    createActivity();
                }
                else{
                    uploadImage(imgUri);
                }

            }
        });

        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilePath();
            }
        });
    }

    /**
     * Se verifica que haya contenido en el campo y se guarda la actividad en la base de datos.
     */
    private void createActivity() {
        if(nombreAct.getText().toString() == "" || etDescripcion.getText().toString() == "" || etTiempo.getText().toString() == ""){
            nombreAct.setError("Required");
            etDescripcion.setError("Required");
            etTiempo.setError("Required");
        }
        else {

            Actividad actividad = new Actividad(nombreAct.getText().toString(), etDescripcion.getText().toString(), Integer.parseInt(etTiempo.getText().toString()), comboDificultad.getSelectedItem().toString(), currentUser.getUid());
            if(imgUri != null){
                //uploadImage(imgUri);
                actividad.setImgUri(imgUri.toString());
            }
            DatabaseReference newRef = reference.push();
            newRef.setValue(actividad).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CrearActividad.this, "Se agrego la actividad", Toast.LENGTH_SHORT).show();
                        nombreAct.getText().clear();
                        etDescripcion.getText().clear();
                        etTiempo.getText().clear();
                        comboDificultad.setSelection(0);


                    }
                    else{
                        Toast.makeText(CrearActividad.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    /**
     * Se invoca el explorador de android para elegir la imagen
     */

    public void getFilePath(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri;
            if(resultData != null){
                uri = resultData.getData();
                Log.i("URI_DATA", uri.toString());
                //uploadImage(uri);
                imgUri = uri;
                Toast.makeText(this, "Se cargó la imagen",Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * Se llama la funcion de google que nos permite subir la imagen al servidor
     * @param uri Path de la imagen en el telefono
     */
    private void uploadImage(Uri uri) {
        final StorageReference imgRef = storageReference.child("images/"+ uri.toString());
        imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imgUri = uri;
                        createActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearActividad.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
