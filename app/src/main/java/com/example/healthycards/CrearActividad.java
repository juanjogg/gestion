package com.example.healthycards;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CrearActividad extends AppCompatActivity {

    private EditText nombreAct, etDescripcion, etDificultad, etTiempo;
    private Button btnEnviar;

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
    }


}
