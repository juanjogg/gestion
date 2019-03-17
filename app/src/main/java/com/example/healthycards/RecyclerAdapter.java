package com.example.healthycards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
            //holder.imageView.setImageDrawable(loadImageFromWeb(actividad.getImgUri()));
            new DownloadImageTask(holder.imageView).execute(actividad.getImgUri());
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


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String url = strings[0];
            Bitmap mIcon = null;

            try {
                InputStream inputStream = new URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());

            }

            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }


    }


}
