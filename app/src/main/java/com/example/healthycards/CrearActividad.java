package com.example.healthycards;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class CrearActividad extends AppCompatActivity {

    private EditText nombreAct, etDescripcion, etDificultad, etTiempo;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_actividad);

        nombreAct = findViewById(R.id.txtnombreActividad);
        etDescripcion = findViewById(R.id.txtdescActividad);
        etDificultad = findViewById(R.id.txtdifiActividad);
        etTiempo = findViewById(R.id.txttiempoActividad);
    }
}
