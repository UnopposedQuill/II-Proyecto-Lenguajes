package view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.ImageAdapter;
import model.ParameterStringBuilder;
import model.Receta;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AgregarReceta extends AppCompatActivity {

    public static String PREFERENCES_FILE_NAME = "preferences.txt";

    //Nombre para los directorios de la aplicación
    private static final String TEMPORAL_PICTURE_NAME = "temp.jpg";
    private static String APP_DIRECTORY = "Recipes/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "RecipesPhotos";

    //Código para petición de uso de cámara, código para tomar una foto con cámara y código para
    //tomar una foto de galería respectivamente
    private final int CAMERA_PERMISSION = 100;
    private final int READ_EXTERNAL_PERMISSION_CODE = 101;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    //Todos los Uri's de la receta actuales, estos apuntan a las imágenes que se van a subir
    private ArrayList<Uri> image_paths;

    //El mostrador de imágenes de la interfaz así como su adaptador de las imágenes a ImageViews
    //private ViewPager image_shower;
    private ImageAdapter image_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);

        //Primero defino los RecyclerView para poder actualizarlos
        //Primero la lista de imágenes
        image_paths = new ArrayList<>();

        //primero creo el mostrador de imágenes
        ViewPager image_shower = findViewById(R.id.image_slider);

        //Ahora el adaptador, encargadado de tomar las imágenes y convertirlas en algo que
        //El mostrador pueda mostrar
        image_adapter = new ImageAdapter();
        image_shower.setAdapter(image_adapter);

        //Ahora el de Ingredientes, necesito conservar el adaptador para poder agregarle datos
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

        //Ahora busco el botón de agregado de imágenes
        final Button mAddImage = findViewById(R.id.boton_agregar_imagen);

        //Le agrego un listener tal que muestre un nuevo diálogo por defecto con 3 opciones desde
        //las cuales se pueden tomar las nuevas imágenes
        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarReceta.this);
                builder.setTitle("Elija la fuente de la nueva imagen");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Tomar foto")) {
                            tomarFoto();
                        } else if (options[which].equals("Elegir de galeria")) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent.createChooser(intent, "Seleccionar App de Imagenees"), SELECT_PICTURE);
                        } else if (options[which].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        //Ahora la funcionalidad de cancelar
        final Button botonCancelar = findViewById(R.id.button_cancelar_creacion);

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Ahora la funcionalidad de agregar la receta como tal
        final Button botonCrearReceta = findViewById(R.id.button_confirm_creation);

        //También necesito los editTexts
        final EditText editTextNombre = findViewById(R.id.editTextNombre);
        final EditText editTextTipo = findViewById(R.id.editTextTipo);

        botonCrearReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO: Hacer que de verdad guarde la receta, primero las validaciones
                /*
                Primero validar que haya ingredientes, instrucciones e imágenes, así como texto en el nombre
                del ingrediente y en el tipo
                */
                if(image_adapter.getCount() > 0 && adaptadorStringsIngredientes.getItemCount() > 0 &&
                        adaptadorStringsInstrucciones.getItemCount() > 0 &&
                        !editTextNombre.getText().toString().equals("") &&
                        !editTextTipo.getText().toString().equals("")){
                    //Procedo a insertar
                    AddRecipeTask addRecipeTask = new AddRecipeTask(
                            new Receta(editTextNombre.getText().toString(), editTextTipo.getText().toString(),
                                    adaptadorStringsIngredientes.getStrings(), adaptadorStringsInstrucciones.getStrings(), image_paths));
                    addRecipeTask.execute();
                    finish();
                }
            }
        });

        //Ahora el botón para agregar un nuevo ingrediente
        final Button botonAgregarIngrediente = findViewById(R.id.button_add_ingredient);

        botonAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextIngrediente = findViewById(R.id.editTextIngrediente);
                String texto = editTextIngrediente.getText().toString();
                if(!texto.equals("")) {
                    adaptadorStringsIngredientes.addData(editTextIngrediente.getText().toString().trim());
                    editTextIngrediente.setText("");
                }
            }
        });

        //Ahora el botón para agregar una nueva instrucción
        final Button botonAgregarInstruccion = findViewById(R.id.button_add_instruction);

        botonAgregarInstruccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextInstruccion = findViewById(R.id.editTextInstrucciones);
                String texto = editTextInstruccion.getText().toString();
                if(!texto.equals("")) {
                    adaptadorStringsInstrucciones.addData(editTextInstruccion.getText().toString().trim());
                    editTextInstruccion.setText("");
                }
            }
        });

        mayRequestReading();

    }

    private boolean mayRequestReading() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            // TODO: alert the user with a Snackbar/AlertDialog giving them the permission rationale
            // To use the Snackbar from the design support library, ensure that the activity extends
            // AppCompatActivity and uses the Theme.AppCompat theme.
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, READ_EXTERNAL_PERMISSION_CODE);
        }
        return false;
    }

    /**
     * Si la parte del listener donde se consigue la información de las imágenes fue exitosa,
     * entonces viene aquí a ver de dónde es que tiene que tomar la información, pues hay dos sitios.
     * @param requestCode El código que llama a esta función, puede ser Cámara o Foto de Galería
     * @param resultCode El código de resultado, si fue exitoso, entonces hago todo
     * @param data La información específica del intento
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PHOTO_CODE: {
                if (resultCode == RESULT_OK) {
                    String dir = Environment.getExternalStorageDirectory() + File.separator +
                            MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
                break;
            }
            case SELECT_PICTURE: {
                if (resultCode == RESULT_OK) {
                    Uri path = data.getData();
                    ImageView imageView = new ImageView(this);
                    imageView.setImageURI(path);
                    image_adapter.addView(imageView);
                    image_paths.add(path);
                    image_adapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    private class AddRecipeTask extends AsyncTask<Void, Void, Boolean> {

        private final Receta receta;
        private final int ADD_RECIPE_CODE = 102;

        public AddRecipeTask(Receta receta) {
            this.receta = receta;
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
                    parameters.put("nombre", receta.getNombre());
                    parameters.put("tipo", receta.getTipo());

                    //Primero especifico el URL al cuál le haré el post de registro
                    URL url = new URL("http://iiproyecto.herokuapp.com/recipe/info?".concat(ParameterStringBuilder.getParamsString(parameters)));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Tipo POST, con 5 segundos de timeout de conexión y de leída de datos
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);

                    if (con.getResponseCode() == 201) {

                        parameters = new HashMap<>();
                        parameters.put("token", token);
                        parameters.put("nombre", receta.getNombre());
                        parameters.put("pasos", receta.instructionsToString());//[Ins 1, Ins 2]
                        parameters.put("ingredientes", receta.ingredientesToString());
                        parameters.put("imagen", receta.imagenesToString());

                        //Primero especifico el URL al cuál le haré el post de registro
                        url = new URL("http://iiproyecto.herokuapp.com/recipe/info?".concat(ParameterStringBuilder.getParamsString(parameters)));
                        con = (HttpURLConnection) url.openConnection();

                        //Tipo POST, con 5 segundos de timeout de conexión y de leída de datos
                        con.setRequestMethod("PUT");
                        con.setConnectTimeout(5000);
                        con.setReadTimeout(5000);
                        if (con.getResponseCode() == 200) {
                            return true;
                        }

                        return false;
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
            if (success) {
                finish();
            }
        }
    }

    //@TODO: Arreglar estas tres funciones, problema al crear el archivo donde se guardará la nueva imagen
    private void tomarFoto() {

        //Primero reviso si tengo los permisos necesarios para usar la cámara
        if (ContextCompat.checkSelfPermission(AgregarReceta.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            //Si no la tengo, entonces procedo a solicitar el permiso

            ActivityCompat.requestPermissions(AgregarReceta.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }else {
            File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            file.mkdirs();

            String path = Environment.getExternalStorageDirectory() + File.separator +
                    MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

            File newFile = new File(path);
            try {
                newFile.createNewFile();
            }
            catch (IOException ex){
                System.out.println("Couldn't create file");
                return;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AgregarReceta.this, AgregarReceta.this.getPackageName() + ".provider", newFile));
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
                    file.mkdirs();

                    String path = Environment.getExternalStorageDirectory() + File.separator +
                            MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

                    File newFile = new File(path);
                    try {
                        newFile.createNewFile();
                    }
                    catch (IOException ex){
                        System.out.println("Couldn't create file");
                        return;
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AgregarReceta.this, AgregarReceta.this.getPackageName() + ".provider", newFile));
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                    startActivityForResult(intent, PHOTO_CODE);
                }
                return;
            }
        }
    }

    private void decodeBitmap(String path) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        image_adapter.addView(imageView);
        image_paths.add(Uri.parse(path));
    }
}
