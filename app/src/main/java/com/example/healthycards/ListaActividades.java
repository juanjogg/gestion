package com.example.healthycards;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaActividades extends AppCompatActivity {
    protected RecyclerView rvActividades;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    private ArrayList<Actividad> data;
    private Button bntCrearActividad, btnSignOut;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent toLogin = new Intent(ListaActividades.this, MainActivity.class);
            Toast.makeText(this, "Debe autenticarse", Toast.LENGTH_SHORT);
            startActivity(toLogin);
            finish();
        }
        Toast.makeText(this, "Bienvenido! " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_actividad);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        data = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        rvActividades = findViewById(R.id.recyclerActividades);
        rvActividades.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvActividades.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(data);
        rvActividades.setAdapter(mAdapter);
        consultarActividades();

        mAuth = FirebaseAuth.getInstance();

        bntCrearActividad = findViewById(R.id.btnCrearActividad);
        btnSignOut = findViewById(R.id.btnSignOut);
        bntCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCrearActividad = new Intent(ListaActividades.this, CrearActividad.class);
                startActivity(toCrearActividad);
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });

    }

    private void signOutUser() {
        mAuth.signOut();
        Intent toMainScreen = new Intent(this, MainActivity.class);
        startActivity(toMainScreen);
    }

    /**
     * Se consultan las actividades en la BD para mostrarlas en la app.
     */
    private void consultarActividades() {
        DatabaseReference db = reference.child("Activity");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.remove(data);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Actividad actividad = ds.getValue(Actividad.class);
                    data.add(actividad);

                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
