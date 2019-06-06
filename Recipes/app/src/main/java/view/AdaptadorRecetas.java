package view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorRecetas extends android.support.v7.widget.RecyclerView.Adapter <AdaptadorRecetas.ViewHolderReceta> {

    //Las recetas que se mostrarán
    private ArrayList<String> nombresRecetas;

    public AdaptadorRecetas(ArrayList<String> nombresRecetas) {
        this.nombresRecetas = nombresRecetas;
    }

    @NonNull
    @Override
    public ViewHolderReceta onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View vistaReceta = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent,false);
        ViewHolderReceta viewHolderReceta = new ViewHolderReceta(vistaReceta);
        return viewHolderReceta;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderReceta viewHolderReceta, int i) {
        viewHolderReceta.textViewNombre.setText(this.nombresRecetas.get(i));
    }

    @Override
    public int getItemCount() {
        return this.nombresRecetas.size();
    }

    //Necesito implementar esta clase, pues es la que define cómo es que se visualizan las recetas
    public static class ViewHolderReceta extends RecyclerView.ViewHolder {

        public TextView textViewNombre;

        public ViewHolderReceta(View v){
            //Creo un ViewHolder Básico
            super(v);

            //Esto es para sincronizar el nombre
            this.textViewNombre = itemView.findViewById(R.id.textViewNombre);

            //Y le agrego lo mío
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //@TODO: Agregar el llamado a detalle de Receta
                }
            });
        }
    }
}
