package view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class BusquedasRecetas extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busquedas_recetas);

        //Primero definir que hará el botón de volver
        final Button buttonDismiss = findViewById(R.id.button_dismiss_search);

        //Simplemente finalizará la actividad
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Ahora la parte complicada, la configuración inicial del RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.recipes_recyclerView);

        //No me interesa que cambie de tamaño
        recyclerView.setHasFixedSize(true);

        //Necesito un administrador de Layout de tipo parrilla con dos columnas
        GridLayoutManager linearLayout = new GridLayoutManager(this, SPAN_COUNT);
        recyclerView.setLayoutManager(linearLayout);

        //Ahora creo un nuevo adaptador de datos que se encargará de pasarle los datos al RecyclerView
        final AdaptadorNombresRecetas adaptadorRecetas = new AdaptadorNombresRecetas(new ArrayList<String>(), this);
        recyclerView.setAdapter(adaptadorRecetas);

        final Button buttonStartSearch = findViewById(R.id.button_start_search);

        buttonStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Primero borrar la información actual del RecyclerView
                adaptadorRecetas.emptyData();

                //Ahora rellenar con la información nueva
                //@TODO: Hacer que estos datos provengan de la API

                String [] datos = {"Huevo Frito", "Huevo Duro"};

                //Ahora muevo todos los valores al adaptador
                for(String s:datos){
                    adaptadorRecetas.addData(s);
                }
            }
        });

    }
}
