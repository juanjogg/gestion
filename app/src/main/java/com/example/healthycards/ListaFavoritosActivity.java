package com.example.healthycards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaFavoritosActivity extends AppCompatActivity {
    private RecyclerView rvFavoritos;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager rvLayout;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ArrayList<String> activityFav;
    private DatabaseReference rootReference;
    private ArrayList<Actividad> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_favoritos);
        mAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        activityFav = new ArrayList<>();
        data = new ArrayList<>();
        rvFavoritos = findViewById(R.id.recyclerFavorito);
        rvFavoritos.setHasFixedSize(true);
        rvLayout = new LinearLayoutManager(this);
        rvFavoritos.setLayoutManager(rvLayout);
        mAdapter = new RecyclerAdapter(data, getIntent().getStringArrayListExtra("userFavorites"));
        rvFavoritos.setAdapter(mAdapter);
        //consultarActividades(getIntent().getStringArrayListExtra("userFavorites"));
        //consultarActividades();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent toLogin = new Intent(ListaFavoritosActivity.this, MainActivity.class);
            Toast.makeText(this, "Debe autenticarse", Toast.LENGTH_SHORT);
            startActivity(toLogin);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        consultarActividades(getIntent().getStringArrayListExtra("userFavorites"));

    }

    private void consultarActividades(ArrayList<String> favs){
        data.clear();
        for(String iterable : favs){
            rootReference.child("Activity").child(iterable).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Actividad act = dataSnapshot.getValue(Actividad.class);
                    act.setActID(dataSnapshot.getKey());
                    data.add(act);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        /*
        rootReference.child("Users").child(currentUser.getUid()).child("Favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    data.add(ds.getValue(Actividad.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        Toast.makeText(this, "Se cargaron los favoritos", Toast.LENGTH_SHORT).show();

    }
}
