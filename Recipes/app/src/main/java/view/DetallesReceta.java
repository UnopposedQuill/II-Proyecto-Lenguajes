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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import model.ImageAdapter;
import model.Receta;

public class DetallesReceta extends AppCompatActivity {

    //Se supone que este recibe una receta al ser creado, e inicializará todos sus valores según eso
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);

        //Primero consigo la información que vino junto con el intento
        Bundle bundle = getIntent().getExtras();
        Receta receta = (Receta) bundle.get("RECETA");

        if(receta == null){
            System.err.println("No se le pasó ninguna receta al activity de detalle");
            finish();
        }

        //primero creo el mostrador de imágenes
        //El mostrador de imágenes de la interfaz así como su adaptador de las imágenes a ImageViews
        ViewPager image_shower = findViewById(R.id.image_slider);

        //Ahora el adaptador, encargadado de tomar las imágenes y convertirlas en algo que
        //El mostrador pueda mostrar
        ImageAdapter image_adapter = new ImageAdapter();
        image_shower.setAdapter(image_adapter);

        //Ahora la funcionalidad de cancelar
        final Button botonCancelar = findViewById(R.id.button_terminar_detalle);

        for(int i = 0;i < receta.getImagenes().size();i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(receta.getImagenes().get(i));
            image_adapter.addView(imageView);
        }

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
