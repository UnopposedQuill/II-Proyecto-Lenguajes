package view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.ImageAdapter;

public class AgregarReceta extends AppCompatActivity {

    //Nombre para los directorios de la aplicación
    private static final String TEMPORAL_PICTURE_NAME = "temp.jpg";
    private static String APP_DIRECTORY = "Recipes/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "RecipesPhotos";

    //Código para petición de uso de cámara, código para tomar una foto con cámara y código para
    //tomar una foto de galería respectivamente
    private final int CAMERA_PERMISSION = 100;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    //Todos los Uri's de la receta actuales, estos apuntan a las imágenes que se van a subir
    private ArrayList<Uri> image_paths;

    //El mostrador de imágenes de la interfaz así como su adaptador de las imágenes a ImageViews
    private ViewPager image_shower;
    private ImageAdapter image_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);

        //primero creo el mostrador de imágenes
        image_shower = findViewById(R.id.image_slider);

        //Ahora el adaptador, encargadado de tomar las imágenes y convertirlas en algo que
        //El mostrador pueda mostrar
        image_adapter = new ImageAdapter();
        image_shower.setAdapter(image_adapter);

        //Primero busco el botón de agregado de imágenes
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
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Seleccionar App de Imagenees"), SELECT_PICTURE);
                        } else if (options[which].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
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
                }
                break;
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
