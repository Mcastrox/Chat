package com.mauricio.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    // Widgets
    EditText mUsername, mPassword;
    Button mIniciarSesion, mNuevo;


    //Firebase

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        // Checking for user existance

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializando Widgets

        mUsername = findViewById(R.id.edtUserInit);
        mPassword = findViewById(R.id.edtPassInit);
        mIniciarSesion = findViewById(R.id.btnIniciar);
        mNuevo = findViewById(R.id.btnNuevo);

        //firebase

        auth = FirebaseAuth.getInstance();


        // No account button
        mNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //login button

        mIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail_txt = mUsername.getText().toString();
                String passInit = mPassword.getText().toString();

                // Checking if it is empty
                if (TextUtils.isEmpty(mail_txt) || TextUtils.isEmpty(passInit)) {

                    Toast.makeText(LoginActivity.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(mail_txt,passInit).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}