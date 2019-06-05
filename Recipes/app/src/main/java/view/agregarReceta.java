package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class agregarReceta extends AppCompatActivity {


    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView ImageView;

    private String mPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);


        ImageView = (ImageView) findViewById(R.id.setPicture);
        final ImageButton mOptionButton = (ImageButton) findViewById(R.id.imageButton_agregar_imagen_receta);

        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence [] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity_agregar_receta.this)

            }
        });



//        Button registerButtonActivity = (Button) findViewById(R.id.button_register_activity);
//        final EditText registerName = (EditText)findViewById(R.id.name_register_activity);
//        final EditText registerEmail = (EditText)findViewById(R.id.email_register_activity);
//        final EditText registerPass = (EditText)findViewById(R.id.password_register_activity);
//
//        registerButtonActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openLogin();
//                Log.v("EditText", registerName.getText().toString());
//                Log.v("EditText", registerEmail.getText().toString());
//                Log.v("EditText", registerPass.getText().toString());
//            }
//        });
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



}
