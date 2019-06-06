package view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        //Primero definir que hará el botón de volver
        final Button buttonDismiss = findViewById(R.id.button_dismiss_recipes);

        //Simplemente finalizará la actividad
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Ahora la parte complicada, la configuración inicial del RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recipes_recyclerView);

        //No me interesa que cambie de tamaño
        recyclerView.setHasFixedSize(true);

        //Necesito un administrador de Layout de tipo parrilla con dos columnas
        GridLayoutManager linearLayout = new GridLayoutManager(this, SPAN_COUNT);
        recyclerView.setLayoutManager(linearLayout);

        //Ahora crearé unos datos de Prueba
        //@TODO: Cambiarlos por una búsqueda usando la API
        ArrayList<String> nombresRecetas = new ArrayList<>();
        nombresRecetas.add("Huevo Frito");
        nombresRecetas.add("Huevo Duro");
        nombresRecetas.add("Fideos");

        //Ahora creo un nuevo adaptador de datos que se encargará de pasarle los datos al RecyclerView
        AdaptadorRecetas adaptadorRecetas = new AdaptadorRecetas(nombresRecetas);
        recyclerView.setAdapter(adaptadorRecetas);
    }
}
