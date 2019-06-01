package view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdministratorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        //ahora importar los parámetros pasados por el llamador
        Bundle bundle = getIntent().getExtras();
        String correoUsuario = bundle.getString("correo");

        TextView textViewNombreUsuario = (TextView) findViewById(R.id.nombreAdministrador);
        textViewNombreUsuario.setText(correoUsuario);
    }
}
