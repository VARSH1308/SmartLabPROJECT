package com.example.smartlab;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class BloodTestActivity<DocumentSnapshot> extends AppCompatActivity {


    FirebaseStorage firebaseStorage;

    FirebaseAuth mAuth;
    RecyclerView mRecyclerView;
    ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    SignUpActivity signUpActivity = new SignUpActivity();
    DatabaseReference databaseReference;
    public String phone;
    DatabaseReference ref,rootRef;
    public String email;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_test);



        user=FirebaseAuth.getInstance().getCurrentUser();
        email=user.getEmail();
        rootRef = FirebaseDatabase.getInstance().getReference();
        ref = rootRef.child("Users");
        ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    phone = datas.getKey();
                    Log.e(phone, "phone:");

                }
                databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+phone+"/BloodTest");


                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String filename = dataSnapshot.getKey();
                        String url = dataSnapshot.getValue(String.class);

                        ((MyAdapter) mRecyclerView.getAdapter()).update(filename, url);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        mRecyclerView = findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(BloodTestActivity.this));
        MyAdapter myAdapter = new MyAdapter(mRecyclerView, BloodTestActivity.this, new ArrayList<String>(), new ArrayList<String>());
        mRecyclerView.setAdapter(myAdapter);


    }
}
