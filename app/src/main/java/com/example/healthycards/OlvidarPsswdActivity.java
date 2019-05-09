package com.example.healthycards;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OlvidarPsswdActivity extends AppCompatActivity {
    private TextView txtViewError;
    private FirebaseAuth mAuth;
    private EditText etEmail;
    private Button btnEnviar;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.olvidar_contrasena);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.txtCorreo);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordReset();
            }
        });

    }

    /**
     * Se envía un correo para el cambio de contraseña.
     */
    private void sendPasswordReset(){
        try{
            if(validateEmail(etEmail.getText().toString())){
                mAuth.sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(OlvidarPsswdActivity.this, "Email sent", Toast.LENGTH_SHORT);
                        }
                        else{

                        }
                    }
                });
            }
            else {
                txtViewError.setText("Wrong email address");
                Toast.makeText(this, "Email erroneo", Toast.LENGTH_SHORT).show();
            }

        }
        catch (IllegalArgumentException e){
            etEmail.setError("Required");
        }

    }

    /**
     * Verifica si se ingresó una dirección de correo electrónico válida
     * @param email
     * @return
     */
    public static boolean validateEmail(String email){
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.find();
    }
}
