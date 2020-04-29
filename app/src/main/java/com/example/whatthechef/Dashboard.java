package com.example.whatthechef;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    TextView name;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    private long backPressedTime;
    private Toast backToast;

    //PRESS BACK AGAIN TO EXIT BEGINS
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast= Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
    //PRESS BACK AGAIN TO EXIT ENDS

    //ON CREATE BEGINS
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        name=findViewById(R.id.namedash);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //GETS THE CURRENT LOGGED IN USER
        try{
            userID= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        //EXTRACTS DATA FROM DATABASE IF USER LOGGED IN VIA EMAIL AND PASSWORD
        if(firebaseAuth.getCurrentUser() !=null){
            DocumentReference documentReference= firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    name.setText(documentSnapshot.getString("Name"));
                }
            });
        }

    }
    //ON CREATE ENDS

    //LOGOUT BUTTON BEGINS
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
    //LOGOUT BUTTON ENDS
}
