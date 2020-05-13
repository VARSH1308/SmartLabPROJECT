package com.example.smartlab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    ProgressBar progressBar;
    private EditText  passwordEt, emailEt, phoneEt, dobEt;
    private Button signUpBtn;
    private RadioGroup radioGroup;
    private RadioButton radioButtonMale,radioButtonFemale;
    private FirebaseAuth mAuth; //firebase auth object
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        passwordEt = findViewById(R.id.password);
        emailEt = findViewById(R.id.email);
        phoneEt = findViewById(R.id.phnumber);
        radioButtonMale = findViewById(R.id.radio_male);
        radioButtonFemale = findViewById(R.id.radio_female);
        dobEt = findViewById(R.id.dob);
        signUpBtn = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance(); //initialise firebase object


        //to check if the user has the account already
        if(mAuth.getCurrentUser()!= null && mAuth.getCurrentUser().isEmailVerified())
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }


        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = month + "/" + dayOfMonth + "/" + year;
                dobEt.setText(date);
            }
        };

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender="";
                final String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                final String phone=  phoneEt.getText().toString().trim();
                final String dob = dobEt.getText().toString().trim();

                if (radioButtonMale.isChecked()){
                    gender = "Male";
                }

                if (radioButtonFemale.isChecked()){
                    gender = "Female";
                }

                if (TextUtils.isEmpty(email)){
                    emailEt.setError("Email is required");
                    emailEt.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordEt.setError("Password is required");
                    passwordEt.requestFocus();
                    return;
                }

                if(password.length()<6)
                {
                    passwordEt.setError("Password must be greater than or equal to six characters");
                    passwordEt.requestFocus();
                    return;
                }

                if(phone.isEmpty())
                {
                    phoneEt.setError("Phone number required");
                    phoneEt.requestFocus();
                    return;
                }

                if(phone.length() != 10)
                {
                    phoneEt.setError("Enter a valid phone number");
                    phoneEt.requestFocus();
                    return;
                }

                if(dob.isEmpty())
                {
                    dobEt.setError("Date of Birth is required");
                    dobEt.requestFocus();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                final String finalGender = gender;
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful())
                                {
                                    //we will store the additional fields in firebase database

                                    User user = new User(
                                            email,
                                            finalGender,
                                            dob
                                    );

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(phone)
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if(task.isSuccessful())
                                            {
                                                //code for email verification
                                                mAuth.getCurrentUser().sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(SignUpActivity.this,"Registered Successfully! Please check your email for verification.",Toast.LENGTH_LONG).show();
                                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

//                                                                    //code for passing value
//                                                                    Intent intent = new Intent(getApplicationContext(),BloodTestActivity.class);
//                                                                    intent.putExtra("phone_number", phone);
//                                                                    startActivity(intent);

                                                                }else {
                                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }else {
                                                Toast.makeText(SignUpActivity.this,"Error!" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(SignUpActivity.this,"Error!" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}
