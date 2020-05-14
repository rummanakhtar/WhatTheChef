package com.example.whatthechef;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class DashBoard extends AppCompatActivity {
    TextView name;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    private long backPressedTime;
    private Toast backToast;
    ProgressBar progressBar;



    //GO TO LOGIN SCREEN
    public void goToLoginScreen(){
        startActivity(new Intent(getApplicationContext(), LogInScreen.class));
        finish();
    }

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
        setContentView(R.layout.activity_dash_board);
        progressBar=findViewById(R.id.progressBar3);
        name= findViewById(R.id.namedash);

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        final GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);

        //GETS THE CURRENT LOGGED IN USER
        try{
            userID= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        //EXTRACTS DATA FROM DATABASE IF USER LOGGED IN VIA EMAIL AND PASSWORD ....BEGINS
        if(firebaseAuth.getCurrentUser() !=null){
            DocumentReference documentReference= firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    name.setText(documentSnapshot.getString("Name"));
                    if(signInAccount !=null){
                        name.setText(firebaseAuth.getCurrentUser().getDisplayName());
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        //EXTRACTS DATA FROM DATABASE IF USER LOGGED IN VIA EMAIL AND PASSWORD ENDS
    }
    //On Create Ends

    //LOGOUT BUTTON BEGINS
    public void logOut(View view) {
        if(firebaseAuth.getCurrentUser() !=null){
            FirebaseAuth.getInstance().signOut();
        }
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
        goToLoginScreen();
    }
    //LOGOUT BUTTON ENDS

    public void goToRecipe(View view){
        startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
    }
}
