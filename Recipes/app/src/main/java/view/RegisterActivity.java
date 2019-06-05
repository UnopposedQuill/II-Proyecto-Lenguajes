package view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button registerButtonActivity = (Button) findViewById(R.id.button_crear_receta);
        final EditText registerName = (EditText)findViewById(R.id.name_register_activity);
        final EditText registerEmail = (EditText)findViewById(R.id.email_register_activity);
        final EditText registerPass = (EditText)findViewById(R.id.password_register_activity);

        registerButtonActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
                Log.v("EditText", registerName.getText().toString());
                Log.v("EditText", registerEmail.getText().toString());
                Log.v("EditText", registerPass.getText().toString());
            }
        });
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void registrarse() {
        // Log.v("EditText", this , registerName.getText().toString());
    }

}
