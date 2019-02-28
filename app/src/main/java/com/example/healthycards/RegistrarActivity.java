package com.example.healthycards;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etPassword;
    private FirebaseAuth mAuth;
    private Button btnEnviar;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        etNombre = findViewById(R.id.txtNombre);
        etApellido = findViewById(R.id.txtApellidos);
        etEmail = findViewById(R.id.txtUsuario);
        etPassword = findViewById(R.id.txtContrasena);
        mAuth = FirebaseAuth.getInstance();
        btnEnviar = findViewById(R.id.btnEnviar);
        txtError = findViewById(R.id.txtError);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().equals("") || etApellido.getText().equals("")){
                    etEmail.setError("Required!");
                    etNombre.setError("Required!");

                    etApellido.setError("Required!");
                    etPassword.setError("Required!");
                }
                else {
                    signUpUser();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void signUpUser() {
        try{
            if(OlvidarPsswdActivity.validateEmail(etEmail.getText().toString())){
                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Ir a la pantalla principal
                            Log.i("Info","TaskCompleted");
                            registerUser(mAuth.getCurrentUser());

                            Toast.makeText(RegistrarActivity.this, "Registracion completa", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(RegistrarActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            Log.e("Exception", task.getException().getMessage());
                            setErrorText(task.getException());
                        }
                    }
                });
            }
            else {
                txtError.setText("Wrong email address");
                Log.e("MAIL_ERROR","Wrong email address");
            }

        }
        catch (IllegalArgumentException e){


            etEmail.setError("Required!");
            etPassword.setError("Required!");

        }
    }

    private void registerUser(FirebaseUser user) {
        User userAdded = new User(etNombre.getText().toString(), etApellido.getText().toString(), etEmail.getText().toString());
        reference.child(user.getUid()).setValue(userAdded);
    }

    private void setErrorText(Exception e){
        txtError.setText(e.getMessage());
    }
}
