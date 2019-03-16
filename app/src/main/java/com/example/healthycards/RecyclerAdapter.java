package com.example.healthycards;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.ViewHolderList>{
    private ArrayList<Actividad> actividades;
    @NonNull
    @Override
    public ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View txtView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
        ViewHolderList vh = new ViewHolderList(txtView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderList holder, int position) {
        Actividad actividad = actividades.get(position);
        if(actividad.getImgUri() == null){
            holder.imageView.setImageResource(R.drawable.defaultimage);

        }
        else{
            holder.imageView.setImageURI(Uri.fromFile(new File(actividad.getImgUri())));
        }
        holder.tiempoActividad.setText("Duración: "+actividades.get(position).getDuracionMin()+" minutos.");
        holder.nombreActividad.setText(actividades.get(position).getNombre());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK","Clickaron un cardView");
            }
        });
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView nombreActividad, tiempoActividad;
        public ImageView imageView;

        public ViewHolderList(View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardView);
            nombreActividad = itemView.findViewById(R.id.nombreActividad);
            tiempoActividad = itemView.findViewById(R.id.tiempoActividad);
            imageView = itemView.findViewById(R.id.imgActividad);
        }
    }

    public RecyclerAdapter(ArrayList<Actividad> dataSet){
        this.actividades = dataSet;
    }

}
