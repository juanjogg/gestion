package com.example.healthycards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_actividad);
        data = new ArrayList<>();
        data.add(new Actividad("Levantamiento de pesas", "Haga repeticiones por 5 minutos", 5, "Media", ""));
        reference = FirebaseDatabase.getInstance().getReference();
        rvActividades = findViewById(R.id.recyclerActividades);
        rvActividades.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvActividades.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(consultarActividades(data));
        rvActividades.setAdapter(mAdapter);


    }

    private ArrayList consultarActividades(final ArrayList datos) {
        DatabaseReference db = reference.child("Activity");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Query query = db.orderByKey().limitToFirst(10);
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
