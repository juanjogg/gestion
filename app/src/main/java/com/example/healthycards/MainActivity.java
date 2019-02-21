package com.example.healthycards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient apiClient;
    private SignInButton signInBtn;
    TextView registro;
    TextView olvPsswd;

    public static final int SIGN_IN_CODE = 777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registro = (TextView)findViewById(R.id.txtRegistro);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(MainActivity.this,RegistrarActivity.class);
                startActivity(registro);
            }
        });

        olvPsswd = (TextView)findViewById(R.id.tvOlvContr);

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
        }
        else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
