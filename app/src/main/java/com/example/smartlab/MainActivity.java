package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rellay1,rellay2;
    private Button signup;
    private Button login;
    private EditText emailEt;
    private EditText pass;
    private Button forgotButton;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 =  (RelativeLayout) findViewById(R.id.relLayout1);
        rellay2 =  (RelativeLayout) findViewById(R.id.relLay2);

        emailEt = findViewById(R.id.email_login);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.Login_Button);
        mAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.SignUp_Button);
        forgotButton = findViewById(R.id.ForgotPass_Button);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //for login

        //to check if the user has the account already
        if(mAuth.getCurrentUser()!= null && mAuth.getCurrentUser().isEmailVerified())
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password =pass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailEt.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is required");
                    return;
                }

                if(password.length()<6)
                {
                    pass.setError("Password must be greater than or equal to six characters");
                }

                //authenticate the user

                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                            }else {
                                Toast.makeText(MainActivity.this, "Please verify your email address.",Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(MainActivity.this,"Login not successful" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //signup button

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        //forgot password
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });

    }



}