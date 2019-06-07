package view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity {

    public static String PREFERENCES_FILE_NAME = "preferences.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButtonActivity = findViewById(R.id.button_crear_receta);
        final EditText registerEmail = findViewById(R.id.email_register_activity);
        final EditText registerPass = findViewById(R.id.password_register_activity);

        registerButtonActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creo una nueva tarea de registro
                RegisterTask registerTask = new RegisterTask(registerEmail.getText().toString(), registerPass.getText().toString(), v.getContext());

                //Ahora la ejecuto
                registerTask.execute();
            }
        });
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context mContext;
        private final int REGISTER_CODE = 101;

        private RegisterTask(String email, String password, Context context){
            mEmail = email;
            mPassword = password;
            this.mContext = context;
        }

        // Do the long-running work in here
        protected Boolean doInBackground(Void... urls) {
            try {
                //Ahora creo un Map de parámetros para enviarlos
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user", mEmail);
                parameters.put("pass", mPassword);

                //Primero especifico el URL al cuál le haré el post de registro
                URL url = new URL("http://iiproyecto.herokuapp.com/login?".concat(ParameterStringBuilder.getParamsString(parameters)));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Tipo POST, con 5 segundos de timeout de conexión y de leída de datos
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                if (con.getResponseCode() == 201) {
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

                    String token = jsonObject.getString("token");

                    //Ahora guardar el token en sharedpreferences
                    SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE).edit();
                    editor.putString("token", token);
                    editor.apply();
                    return true;
                }
                return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(final Boolean success) {
            if(success){
                Intent intent = new Intent(this.mContext, UserHome.class);
                startActivityForResult(intent, REGISTER_CODE);
            }
        }
    }
}
