package com.example.healthycards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class CaracteristicaActividad extends AppCompatActivity {
    private ImageView imgActividad;
    private TextView descripcionAct, duracionAct, dificultadAct, nombreAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caracteristicas_actividad);
        Bundle extra = getIntent().getExtras();
        descripcionAct = findViewById(R.id.lbl_descripcion);
        duracionAct = findViewById(R.id.lbl_duracion);
        dificultadAct = findViewById(R.id.lbl_dificultad);
        imgActividad = findViewById(R.id.img_actividad);
        nombreAct = findViewById(R.id.lbl_nombreActividad);
        imgActividad = findViewById(R.id.img_actividad);
        descripcionAct.setText(extra.getString("ActivityDescription"));
        duracionAct.setText(extra.getInt("ActivityDuration")+"");
        dificultadAct.setText(extra.getString("ActivityLevel"));
        nombreAct.setText(extra.getString("ActivityName"));

        if(extra.getString("ActivityImage") == ""){
            imgActividad.setImageResource(R.drawable.defaultimage);
        }
        else {
            new RecyclerAdapter.DownloadImageTask(imgActividad).execute(extra.getString("ActivityImage"));
        }


    }
}
