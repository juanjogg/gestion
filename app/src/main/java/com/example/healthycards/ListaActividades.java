package com.example.healthycards;

import android.content.Intent;
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

public class ListaActividades extends AppCompatActivity {
    private RecyclerView rvActividades;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Actividad> data;
    private Button bntCrearActividad;
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
        mAdapter = new RecyclerAdapter(consultarActividades(data));
        rvActividades.setAdapter(mAdapter);
        mAuth = FirebaseAuth.getInstance();
        
        bntCrearActividad = findViewById(R.id.btnCrearActividad);

        bntCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCrearActividad = new Intent(ListaActividades.this, CrearActividad.class);
                startActivity(toCrearActividad);
            }
        });

    }

    private ArrayList consultarActividades(final ArrayList datos) {
        DatabaseReference db = reference.child("Activity");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                for(DataSnapshot ds : dataSnapshots){
                    Actividad actividad = ds.getValue(Actividad.class);
                    datos.add(actividad);

                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return datos;

    }
}
