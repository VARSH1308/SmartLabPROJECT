package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private EditText userEmail;
    private Button userPass;
    private Button userLogin;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.emailForgot);
        userPass = findViewById(R.id.btnForgotPass);
        mauth = FirebaseAuth.getInstance();
        userLogin = findViewById(R.id.btnLoginPass);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);


        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
            mauth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this,"Password sent to your email",Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(ForgotPasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            });
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}
