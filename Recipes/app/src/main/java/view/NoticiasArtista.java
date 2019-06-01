package view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoticiasArtista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias_artista);

        //ahora importar los parámetros pasados por el llamador
        Bundle bundle = getIntent().getExtras();
        String correoUsuario = bundle.getString("correo");

        TextView textViewNombreUsuario = (TextView) findViewById(R.id.nombreAdministrador);
        textViewNombreUsuario.setText(correoUsuario);
    }
}
