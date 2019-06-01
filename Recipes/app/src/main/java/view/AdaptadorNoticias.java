package view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Noticia;
import model.NoticiaAdministrador;
import model.NoticiaArtista;

public class AdaptadorNoticias extends RecyclerView.Adapter <AdaptadorNoticias.ViewHolderNoticia> {

    private ArrayList<Noticia> mDataset; //las noticias que se mostrarán

    public static class ViewHolderNoticia extends RecyclerView.ViewHolder{

        public TextView textViewDescripcion;
        public TextView textViewAbstract;
        public TextView textViewFechaPublicacion;
        public TextView textViewAutor;

        public ViewHolderNoticia(View itemView) {
            super(itemView);
            this.textViewAbstract = itemView.findViewById(R.id.textViewAbstract);
            this.textViewAutor = itemView.findViewById(R.id.textViewAutor);
            this.textViewFechaPublicacion = itemView.findViewById(R.id.textViewFechaPublicacion);
            this.textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
        }
    }

    public AdaptadorNoticias(ArrayList<Noticia> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public AdaptadorNoticias.ViewHolderNoticia onCreateViewHolder(ViewGroup parent, int i) {
        View vistaNoticia = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_noticia, parent,false);
        ViewHolderNoticia viewHolderNoticia = new ViewHolderNoticia(vistaNoticia);
        return viewHolderNoticia;
    }

    @Override
    public void onBindViewHolder( AdaptadorNoticias.ViewHolderNoticia viewHolder, int i) {
        viewHolder.textViewAbstract.setText(this.mDataset.get(i).getAbstractNoticia());
        viewHolder.textViewDescripcion.setText(this.mDataset.get(i).getDescripcion());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.textViewFechaPublicacion.setText(sdf.format(this.mDataset.get(i).getFechaPublicacion()));
        switch (this.mDataset.get(i).getTipoNoticia()){
            case ADMINISTRADOR:{
                viewHolder.textViewAutor.setText(((NoticiaAdministrador)this.mDataset.get(i)).getAdministradorPublicador().getNombre());
                break;
            }
            case ARTISTA:{
                viewHolder.textViewAutor.setText(((NoticiaArtista)this.mDataset.get(i)).getArtistaPublicador().getNombre());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }
}
