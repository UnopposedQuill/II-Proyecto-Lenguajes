package view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import model.ParameterStringBuilder;

public class UserHome extends AppCompatActivity {

    private final int CODIGO_USER_HOME = 101;
    public static String PREFERENCES_FILE_NAME = "preferences.txt";

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
                LogoutTask logoutTask = new LogoutTask();
                logoutTask.execute();
            }
        });

        botonBuscarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BusquedasRecetas.class);
                startActivityForResult(intent, CODIGO_USER_HOME);
            }
        });
    }

    private class LogoutTask extends AsyncTask<Void, Void, Boolean> {

        // Do the long-running work in here
        protected Boolean doInBackground(Void... urls) {
            SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);
            String token = prefs.getString("token", null);
            if(token != null) {
                try {
                    //Ahora creo un Map de parámetros para enviarlos
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("token", token);

                    //Primero especifico el URL al cuál le haré el post de registro
                    URL url = new URL("http://iiproyecto.herokuapp.com/login?".concat(ParameterStringBuilder.getParamsString(parameters)));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Tipo POST, con 5 segundos de timeout de conexión y de leída de datos
                    con.setRequestMethod("PUT");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);

                    if (con.getResponseCode() == 200) {
                        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        return true;
                    }
                    return false;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(final Boolean success) {
            if(success){
                finish();
            }
        }
    }
}
