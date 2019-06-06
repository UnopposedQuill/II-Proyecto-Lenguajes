package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserHome extends AppCompatActivity {

    private final int CODIGO_USER_HOME = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //Todos los botones que posee la interfaz
        final Button botonCrearReceta = findViewById(R.id.button_Agregar_Receta);
        final Button botonCerrarSesion = findViewById(R.id.button_cerrar_sesion);
        final Button botonTodasRecetas = findViewById(R.id.button_Listar_Recetas);
        final Button botonBuscarReceta = findViewById(R.id.button_Buscar_Receta);

        //Ahora las funcionalidades de los botones
        botonCrearReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AgregarReceta.class);
                startActivityForResult(intent, CODIGO_USER_HOME);
            }
        });

        botonTodasRecetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Recipes.class);
                startActivityForResult(intent, CODIGO_USER_HOME);
            }
        });

        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO: Hacer que de verdad cierre sesión
                finish();
            }
        });

        botonBuscarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO: Hacer que redirija a una versión especializada de la que muestra todas las recetas
                System.out.println("Non implemented yet");
            }
        });
    }
}
