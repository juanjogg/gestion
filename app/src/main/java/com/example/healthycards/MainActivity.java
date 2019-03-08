package com.example.healthycards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient apiClient;
    private SignInButton signInBtn;
    private TextView registro, olvPsswd;
    private Button btnIngresar;
    private FirebaseAuth mAuth;
    private EditText etMail, etPassword;
    public static final int SIGN_IN_CODE = 777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        etMail = findViewById(R.id.txtUsr);
        etPassword = findViewById(R.id.txtPasswd);

        registro = findViewById(R.id.txtRegistro);
        btnIngresar = findViewById(R.id.btnIngresar);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(MainActivity.this,RegistrarActivity.class);
                startActivity(registro);
            }
        });

        olvPsswd = findViewById(R.id.tvOlvContr);

        olvPsswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent olvPsswd = new Intent(MainActivity.this,OlvidarPsswdActivity.class);
                startActivity(olvPsswd);
            }
        });

        signInBtn = findViewById(R.id.signInButton);
        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gsOptions).build();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
    }

    private void signInUser() {
        try{
            if (OlvidarPsswdActivity.validateEmail(etMail.getText().toString())) {
                mAuth.signInWithEmailAndPassword(etMail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Te has logueado", Toast.LENGTH_SHORT).show();
                            Intent toMainList = new Intent(MainActivity.this, CrearActividad.class);
                            startActivity(toMainList);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Email o password erroneos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(this, "Email incorrecto", Toast.LENGTH_SHORT).show();
            }

        }
        catch (IllegalArgumentException e){
            etMail.setError("Required!");
            etPassword.setError("Required!");
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error en la conexion del servidor " + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            Toast.makeText(this, "Te has logueado con Google!", Toast.LENGTH_SHORT).show();
            Intent toMainList = new Intent(MainActivity.this, CrearActividad.class);
            startActivity(toMainList);
            finish();
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
