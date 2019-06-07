package view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.ParameterStringBuilder;

public class Recipes extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    public static String PREFERENCES_FILE_NAME = "preferences.txt";

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

        //Ahora la parte complicada, primero la configuración inicial del RecyclerView de Imágenes
        RecyclerView recyclerViewImagenes = findViewById(R.id.recipes_recyclerView);

        //No me interesa que cambie de tamaño
        recyclerViewImagenes.setHasFixedSize(true);

        //Necesito un administrador de Layout de tipo parrilla con dos columnas
        GridLayoutManager linearLayout = new GridLayoutManager(this, SPAN_COUNT);
        recyclerViewImagenes.setLayoutManager(linearLayout);

        //Ahora crearé unos datos de Prueba
        //@TODO: Cambiarlos por una búsqueda usando la API
        ArrayList<String> nombresRecetas = new ArrayList<>();

        //Ahora creo un nuevo adaptador de datos que se encargará de pasarle los datos al RecyclerView
        AdaptadorNombresRecetas adaptadorRecetas = new AdaptadorNombresRecetas(nombresRecetas, this);
        FillRecipesTask fillRecipesTask = new FillRecipesTask(adaptadorRecetas);
        fillRecipesTask.execute();
        recyclerViewImagenes.setAdapter(adaptadorRecetas);
    }

    private class FillRecipesTask extends AsyncTask<Void, Void, Boolean> {

        private AdaptadorNombresRecetas adaptadorRecetas;

        public FillRecipesTask(AdaptadorNombresRecetas adaptadorRecetas) {
            this.adaptadorRecetas = adaptadorRecetas;
        }

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
                    URL url = new URL("http://iiproyecto.herokuapp.com/recipe?".concat(ParameterStringBuilder.getParamsString(parameters)));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Tipo POST, con 5 segundos de timeout de conexión y de leída de datos
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);

                    if (con.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();

                        String s = content.toString();
                        //System.out.println(s);

                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArrayRecipes = jsonObject.getJSONArray("recetas");
                        for(int i = 0; i < jsonArrayRecipes.length();i++){
                            String recipe = jsonArrayRecipes.getString(i);
                            this.adaptadorRecetas.addData(recipe);
                        }
                        return true;
                    }
                    return false;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(final Boolean success) {
            if(success){
                adaptadorRecetas.notifyDataSetChanged();
            }
        }
    }
}
