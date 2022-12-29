package com.example.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class login extends AppCompatActivity {
    Button btn_login;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Log in succesful", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(login.this, Dashboard.class);
//                startActivity(intent);
//            }
//        });
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_pass);
        fAuth= FirebaseAuth.getInstance();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Login Requirements
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();

                if(TextUtils.isEmpty(Email))
                {
                    email.setError("An email is needed");
                    return;
                }
                if(TextUtils.isEmpty(Pass))
                {
                    password.setError("A password is needed");
                    return;
                }
                if(Pass.length()<6)
                {
                    password.setError("Passwords must be 6 characters or more");
                    return;
                }
//                load.setVisibility(View.VISIBLE);
                    //Authentication process
                    fAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                if(Email.contentEquals("root@gmail.com")&& Pass.contentEquals("rooter")){
                                    startActivity(new Intent(getApplicationContext(),admin.class));
                                }
                                else{
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                }
                            }
                            else {
                                Toast.makeText(login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            load.setVisibility(View.GONE);
                            }
                        }
                    });

            }
        });

    }
}