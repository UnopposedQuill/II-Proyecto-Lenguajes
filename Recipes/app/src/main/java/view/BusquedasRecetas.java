package view;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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

public class BusquedasRecetas extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    public static String PREFERENCES_FILE_NAME = "preferences.txt";

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
                EditText textoNombre = findViewById(R.id.editTextValorBusqueda);
                String valor = textoNombre.getText().toString();

                int valorSeleccion = 0;
                RadioButton radioButtonNombre = findViewById(R.id.radioButtonNombre);
                if(radioButtonNombre.isChecked()){ valorSeleccion=1; }
                RadioButton radioButtonIngre = findViewById(R.id.radioButtonIngrediente);
                if(radioButtonIngre.isChecked()){ valorSeleccion=2; }
                RadioButton radioButtonTipo = findViewById(R.id.radioButtonTipo);
                if(radioButtonTipo.isChecked()){ valorSeleccion=3; }


                //@TODO: Hacer que estos datos provengan de la API
                FillRecipesTask fillRecipesTask = new FillRecipesTask(adaptadorRecetas,valorSeleccion,valor);
                fillRecipesTask.execute();
            }
        });

    }

    private class FillRecipesTask extends AsyncTask<Void, Void, Boolean> {

        private AdaptadorNombresRecetas adaptadorRecetas;
        private int Seleccion;
        private  String Valor;
        public FillRecipesTask(AdaptadorNombresRecetas adaptadorRecetas, int Seleccion, String Valor) {
            this.adaptadorRecetas = adaptadorRecetas;
            this.Seleccion = Seleccion;
            this.Valor = Valor;
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
                    if(this.Seleccion == 1) parameters.put("nombre", this.Valor);
                    if(this.Seleccion == 2) parameters.put("ingrediente", this.Valor);
                    if(this.Seleccion == 3) parameters.put("tipo", this.Valor);


                    //Primero especifico el URL al cuál le haré el post de registro
                    URL url = new URL("http://iiproyecto.herokuapp.com/recipe/filter?".concat(ParameterStringBuilder.getParamsString(parameters)));
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
                        JSONArray jsonArrayRecipes = jsonObject.getJSONArray("result");
                        for(int i = 0; i < jsonArrayRecipes.length();i++){
                            String recipe = jsonArrayRecipes.getString(i);
                            this.adaptadorRecetas.addData(recipe);
                        }

                        //Ahora muevo todos los valores al adaptador
                        /**for(String s:datos){
                            adaptadorRecetas.addData(s);
                        }**/

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
