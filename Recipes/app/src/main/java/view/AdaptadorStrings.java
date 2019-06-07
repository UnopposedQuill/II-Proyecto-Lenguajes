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

public class AdaptadorStrings extends RecyclerView.Adapter <AdaptadorStrings.ViewHolderString> {

    //Las recetas que se mostrarán
    private ArrayList<String> strings;

    public AdaptadorStrings(ArrayList<String> strings) {
        this.strings = strings;
    }

    /**
     * Para vaciar la lista de datos
     */
    public void emptyData(){
        //Mientras haya datos
        while(!this.strings.isEmpty()){
            //Remueva
            this.strings.remove(0);
        }
        //Notificar cambios
        this.notifyDataSetChanged();
    }

    public void addData(String s){
        this.strings.add(s);
        notifyDataSetChanged();
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    @NonNull
    @Override
    public ViewHolderString onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View vistaReceta = LayoutInflater.from(parent.getContext()).inflate(R.layout.string_layout, parent,false);
        return new ViewHolderString(vistaReceta);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderString ViewHolderString, int i) {
        ViewHolderString.textViewString.setText(this.strings.get(i));
    }

    @Override
    public int getItemCount() {
        return this.strings.size();
    }

    //Necesito implementar esta clase, pues es la que define cómo es que se visualizan las recetas
    public static class ViewHolderString extends RecyclerView.ViewHolder {

        public TextView textViewString;

        ViewHolderString(View v){
            //Creo un ViewHolder Básico
            super(v);

            //Esto es para sincronizar el nombre
            this.textViewString = itemView.findViewById(R.id.textViewNombre);
        }
    }
}
