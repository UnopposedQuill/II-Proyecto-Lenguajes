package view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorNombresRecetas extends android.support.v7.widget.RecyclerView.Adapter <AdaptadorNombresRecetas.ViewHolderReceta> {

    //Las recetas que se mostrarán
    private ArrayList<String> nombresRecetas;
    private Activity activity;

    public AdaptadorNombresRecetas(ArrayList<String> nombresRecetas, Activity activity) {
        this.nombresRecetas = nombresRecetas;
        this.activity = activity;
    }

    /**
     * Para vaciar la lista de datos
     */
    public void emptyData(){
        //Mientras haya datos
        while(!this.nombresRecetas.isEmpty()){
            //Remueva
            this.nombresRecetas.remove(0);
        }
        //Notificar cambios
        this.notifyDataSetChanged();
    }

    public boolean addData(String s){
        boolean b = this.nombresRecetas.add(s);
        notifyDataSetChanged();
        return b;
    }

    @NonNull
    @Override
    public ViewHolderReceta onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View vistaReceta = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent,false);
        ViewHolderReceta viewHolderReceta = new ViewHolderReceta(vistaReceta, this.activity);
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
        protected final int CODIGO_RECYCLER_ADAPTER = 100;

        public ViewHolderReceta(View v, final Activity activity){
            //Creo un ViewHolder Básico
            super(v);

            //Esto es para sincronizar el nombre
            this.textViewNombre = itemView.findViewById(R.id.textViewNombre);

            //Y le agrego lo mío
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //@TODO: Agregar el llamado a detalle de Receta
                    //Creo el intento de llamado de Actividad
                    Intent intent = new Intent(v.getContext(), DetallesReceta.class);

                    //Ahora agrego el extra
                    intent.putExtra("NOMBRE_RECETA", textViewNombre.getText().toString());

                    //Y hago la llamada
                    activity.startActivityForResult(intent, CODIGO_RECYCLER_ADAPTER);
                }
            });
        }
    }
}
