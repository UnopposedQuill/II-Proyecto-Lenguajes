package view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.ImageAdapter;
import model.ParameterStringBuilder;
import model.Receta;

public class DetallesReceta extends AppCompatActivity {

    public static String PREFERENCES_FILE_NAME = "preferences.txt";
    private String tipoBuscado;

    //Se supone que este recibe una receta al ser creado, e inicializará todos sus valores según eso
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);

        //Primero consigo la información que vino junto con el intento
        String nombreReceta = getIntent().getExtras().getString("NOMBRE_RECETA", "NOT FOUND");

        //Ahora comienzo con los RecyclerViews, primero el de Ingredientes, necesito conservar el
        //adaptador para poder agregarle datos
        RecyclerView recyclerViewIngredientes = findViewById(R.id.recyclerViewIngredientes);

        recyclerViewIngredientes.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewIngredientes.setLayoutManager(linearLayoutManager);

        final AdaptadorStrings adaptadorStringsIngredientes = new AdaptadorStrings(new ArrayList<String>());
        recyclerViewIngredientes.setAdapter(adaptadorStringsIngredientes);

        //Finalmente el de Instrucciones
        RecyclerView recyclerViewInstrucciones = findViewById(R.id.recyclerViewInstrucciones);

        recyclerViewInstrucciones.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewInstrucciones.setLayoutManager(linearLayoutManager);

        final AdaptadorStrings adaptadorStringsInstrucciones = new AdaptadorStrings(new ArrayList<String>());
        recyclerViewInstrucciones.setAdapter(adaptadorStringsInstrucciones);

        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewTipo = findViewById(R.id.textViewTipo);

        textViewNombre.setText(nombreReceta);
        textViewTipo.setText("");

        //primero creo el mostrador de imágenes
        //El mostrador de imágenes de la interfaz así como su adaptador de las imágenes a ImageViews
        ViewPager image_shower = findViewById(R.id.image_slider);

        //Ahora el adaptador, encargadado de tomar las imágenes y convertirlas en algo que
        //El mostrador pueda mostrar
        ImageAdapter image_adapter = new ImageAdapter();
        FetchRecipeTask fetchRecipeTask = new FetchRecipeTask(image_adapter, adaptadorStringsIngredientes, adaptadorStringsInstrucciones, nombreReceta, this);
        fetchRecipeTask.execute();

        image_shower.setAdapter(image_adapter);

        //Ahora la funcionalidad de cancelar
        final Button botonCancelar = findViewById(R.id.button_terminar_detalle);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class FetchRecipeTask extends AsyncTask<Void, Void, Boolean> {

        private final ImageAdapter imageAdapter;
        private final AdaptadorStrings ingredientes;
        private final AdaptadorStrings instrucciones;
        private final String nombreReceta;
        private final Context context;

        public FetchRecipeTask(ImageAdapter imageAdapter, AdaptadorStrings ingredientes, AdaptadorStrings instrucciones, String nombreReceta, Context context) {
            this.imageAdapter = imageAdapter;
            this.ingredientes = ingredientes;
            this.instrucciones = instrucciones;
            this.nombreReceta = nombreReceta;
            this.context = context;
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
                    parameters.put("nombre", nombreReceta);

                    //Primero especifico el URL al cuál le haré el post de registro
                    URL url = new URL("http://iiproyecto.herokuapp.com/recipe/info?".concat(ParameterStringBuilder.getParamsString(parameters)));
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
                        String tipo = jsonObject.getString("tipo");

                        JSONArray jsonArrayIngredientes = jsonObject.getJSONArray("ingrediente");
                        JSONArray jsonArrayInstrucciones = jsonObject.getJSONArray("pasos");
                        JSONArray jsonArrayImagenes = jsonObject.getJSONArray("imagenes");

                        for(int i = 0; i < jsonArrayIngredientes.length();i++){
                            String ingrediente = jsonArrayIngredientes.getString(i);
                            ingredientes.addData(ingrediente);
                        }

                        for(int i = 0; i < jsonArrayInstrucciones.length();i++){
                            String instruccion = jsonArrayInstrucciones.getString(i);
                            instrucciones.addData(instruccion);
                        }

                        for(int i = 0; i < jsonArrayImagenes.length();i++){
                            String image = jsonArrayImagenes.getString(i);
                            ImageView imageView = new ImageView(this.context);
                            //imageView.setImageURI(URI.create(image));
                            this.imageAdapter.addView(imageView);
                        }

                        tipoBuscado = tipo;
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
            if (success) {
                imageAdapter.notifyDataSetChanged();
                ingredientes.notifyDataSetChanged();
                instrucciones.notifyDataSetChanged();
                TextView textViewTipo = findViewById(R.id.textViewTipo);
                textViewTipo.setText(tipoBuscado);
            }
        }
    }
}
