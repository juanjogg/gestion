package com.example.healthycards;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CrearActividad extends AppCompatActivity {

    private EditText nombreAct, etDescripcion, etDificultad, etTiempo;
    private Button btnEnviar;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

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

    Spinner comboDificultad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_actividad);


        comboDificultad = findViewById(R.id.spinDifiActividad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.combo_dificultades, android.R.layout.simple_spinner_item);
        comboDificultad.setAdapter(adapter);


        nombreAct = findViewById(R.id.txtnombreActividad);
        etDescripcion = findViewById(R.id.txtdescActividad);

        //OJO: Hacer casteo acá para poder leer el dato.
        //etDificultad = findViewById(R.id.spinDifiActividad);

        etTiempo = findViewById(R.id.txttiempoActividad);
        btnEnviar = findViewById(R.id.btnCrearActividad);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Activity");

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity();
            }
        });
    }

    private void createActivity() {
        if(nombreAct.getText().toString() == "" || etDescripcion.getText().toString() == "" || etTiempo.getText().toString() == ""){
            nombreAct.setError("Required");
            etDescripcion.setError("Required");
            etTiempo.setError("Required");
        }
        else {
            Actividad actividad = new Actividad(nombreAct.getText().toString(), etDescripcion.getText().toString(), Integer.parseInt(etTiempo.getText().toString()), etDificultad.getText().toString(), currentUser.getUid());
            DatabaseReference newRef = reference.push();
            newRef.setValue(actividad).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CrearActividad.this, "Se agrego la actividad", Toast.LENGTH_SHORT).show();
                        nombreAct.getText().clear();
                        etDescripcion.getText().clear();
                        etTiempo.getText().clear();
                        etDificultad.getText().clear();
                    }
                    else{
                        Toast.makeText(CrearActividad.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


}
