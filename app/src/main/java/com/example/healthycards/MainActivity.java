package com.example.healthycards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient apiClient;
    private SignInButton signInBtn;
    private TextView registro, olvPsswd;
    private Button btnIngresar;
    private FirebaseAuth mAuth;
    private EditText etMail, etPassword;
    public static final int SIGN_IN_CODE = 777;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Intent toList = new Intent(MainActivity.this, ListaActividades.class);
            startActivity(toList);
            finish();
        }
    }

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
        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


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

    /**
     * Se valida que los campos no estén vacios y se registra al usuario por medio de su correo y contraseña
     */
    private void signInUser() {
        try{
            if (OlvidarPsswdActivity.validateEmail(etMail.getText().toString())) {
                mAuth.signInWithEmailAndPassword(etMail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Te has logueado", Toast.LENGTH_SHORT).show();
                            Intent toMainList = new Intent(MainActivity.this, ListaActividades.class);
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
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount googleAccount = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(googleAccount);
            }
            catch (ApiException e){
                Log.w("LOGIN_FAILED",e);
            }

        }
    }

    /**
     * Verifica si la conexión con los sevidores de google fue efectiva y se realiza el login al usuario
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            Toast.makeText(this, "Te has logueado con Google!", Toast.LENGTH_SHORT).show();
            Intent toMainList = new Intent(MainActivity.this, ListaActividades.class);
            startActivity(toMainList);
            finish();
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Integra el servicio el login de Google con el de FireBase dando inicio a la sesión del usuario.
     * @param account cuenta de Google
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(MainActivity.this, "Te has logueado con Google!", Toast.LENGTH_SHORT).show();
                            Intent toMainList = new Intent(MainActivity.this, ListaActividades.class);
                            startActivity(toMainList);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Auth failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
