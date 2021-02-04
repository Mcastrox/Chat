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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Widgets
    EditText mName , mPassword, mEmail;
    Button mRegister;
    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializando Widgets
        mName = findViewById(R.id.edtName);
        mPassword = findViewById(R.id.edtPass);
        mEmail = findViewById(R.id.edtEmail);
        mRegister = findViewById(R.id.btnRegister);

        // Inicializando Firebase

        auth = FirebaseAuth.getInstance();

        //Adding evnt listener to button register
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_txt = mName.getText().toString();
                String emal_txt = mEmail.getText().toString();
                String password_txt = mPassword.getText().toString();

                if (TextUtils.isEmpty(username_txt) || TextUtils.isEmpty(emal_txt) || TextUtils.isEmpty(password_txt)) {

                    Toast.makeText(RegisterActivity.this, "Por favor llena todaos los campos ", Toast.LENGTH_LONG).show();
                }
                else{
                    registerNow(username_txt,emal_txt,password_txt);
                }


            }
        });
    }

    private void registerNow(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId);

                            //Hashmaps
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            //Opening mainactivity after succesfull register
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }else{
                            Toast.makeText(RegisterActivity.this, "Correo no valido o contraseña invalida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}