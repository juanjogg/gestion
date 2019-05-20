package com.example.healthycards;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CaracteristicaActividad extends AppCompatActivity {
    private ImageView imgActividad;
    private ImageButton btnAddFav;
    private TextView descripcionAct, duracionAct, dificultadAct, nombreAct, tiempo;
    private DatabaseReference rootReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<String> userFavorites;
    private Button btnTiempo;
    private CountDownTimer countDownTimer;
    private static final int MILIS = 60000;
    private long tiempoEnMilisegundos;
    private boolean tiempoCorriendo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caracteristicas_actividad);
        final Bundle extra = getIntent().getExtras();
        btnAddFav = findViewById(R.id.btn_favoritos);
        descripcionAct = findViewById(R.id.lbl_descripcion);
        duracionAct = findViewById(R.id.lbl_duracion);
        dificultadAct = findViewById(R.id.lbl_dificultad);
        imgActividad = findViewById(R.id.img_actividad);
        nombreAct = findViewById(R.id.lbl_nombreActividad);
        imgActividad = findViewById(R.id.img_actividad);
        descripcionAct.setText(extra.getString("ActivityDescription"));
        duracionAct.setText(extra.getInt("ActiviyDuration")+"");
        Log.i("DURACION",extra.getInt("ActiviyDuration") + "");
        tiempoEnMilisegundos = MILIS * extra.getInt("ActiviyDuration");
        dificultadAct.setText(extra.getString("ActivityLevel"));
        nombreAct.setText(extra.getString("ActivityName"));
        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userFavorites = extra.getStringArrayList("UserFavorites");
        tiempo = findViewById(R.id.lbl_Cronometro);
        btnTiempo = findViewById(R.id.btn_comenzar);

        btnTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empezarParar();
            }
        });

        actualizarTiempo();

        if(extra.getString("ActivityImage") == ""){
            imgActividad.setImageResource(R.drawable.defaultimage);
        }
        else {
            new RecyclerAdapter.DownloadImageTask(imgActividad).execute(extra.getString("ActivityImage"));
        }

        btnAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actividad actividad = new Actividad(extra.getString("ActivityName"), extra.getString("ActivityDescription"), extra.getInt("ActivityDuration"),extra.getString("ActivityLevel"), extra.getString("ActivityID"));


                if(!checkFavs(extra.getString("ActivityID")))
                    addFavorite(extra.getString("ActivityID"));
                else 
                    deleteFavorite(extra.getString("ActivityID"));
            }
        });


    }

    public void empezarParar(){
        if(tiempoCorriendo){
            pararTiempo();
        } else{
            empezarTiempo();
        }
    }

    public void empezarTiempo(){
        countDownTimer = new CountDownTimer(tiempoEnMilisegundos, 1000) {
            @Override
            public void onTick(long l) {
                tiempoEnMilisegundos = l;
                actualizarTiempo();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        btnTiempo.setText("Pausar");

        tiempoCorriendo = true;
    }

    public void pararTiempo(){
        countDownTimer.cancel();
        btnTiempo.setText("Comenzar");
        tiempoCorriendo = false;
    }

    public void actualizarTiempo(){
        int minutos = (int) tiempoEnMilisegundos / 60000;
        int segundos = (int) tiempoEnMilisegundos % 60000 / 1000;

        String textoTiempo;
        textoTiempo = "" + minutos;
        textoTiempo += ":";

        if(segundos < 10)
            textoTiempo += "0";
        textoTiempo += segundos;

        tiempo.setText(textoTiempo);
    }

    private void deleteFavorite(String actID) {
        //Codigo anterior recibe string con idActividad
        userFavorites.remove(actID);
        rootReference.child("Users").child(user.getUid()).child("Favorites").orderByValue().equalTo(actID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        rootReference.child("Users").child(user.getUid()).child("Favorites").orderByChild("uID").equalTo(activity.getuID()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userFavorites.remove(activity.getuID());
        */
        Toast.makeText(this,"Se eliminó de favoritos", Toast.LENGTH_SHORT).show();
    }

    private void addFavorite(String actID) {
        //Codigo anterior recibe string con idActividad
        userFavorites.add(actID);
        rootReference.child("Users").child(user.getUid()).child("Favorites").push().setValue(actID);

        /*userFavorites.add(activity.getuID());
        DatabaseReference reference = rootReference.child("Users").child(user.getUid()).child("Favorites").push();
        reference.setValue(activity);
        */
        Toast.makeText(this,"Se agregó favorito",Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if the actual activity is one of the user's favorites
     * @param actID Activity ID
     */
    private boolean checkFavs(String actID){
        for(String iterable : userFavorites){
            if(iterable.equals(actID)){
                return true;
            }
        }
        return false;
    }
}
