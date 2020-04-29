package com.example.whatthechef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogInScreen extends AppCompatActivity {
    public static final int GOOGLE_SIGN_IN_CODE = 10000;
    EditText email,password;
    Button signInButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private long backPressedTime;
    private Toast backToast;
    GoogleSignInClient signInClient;
    GoogleSignInOptions gso;
    SignInButton signIn;

    //GO TO DASHBOARD
    public void callDashboard(){
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
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
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LINKING THE XML ELEMENTS WITH THEIR RESPECTIVE JAVA VARIABLES
        email=findViewById(R.id.emailsignin);
        password=findViewById(R.id.passwordsignin);
        signInButton=findViewById(R.id.signinbutton);
        progressBar=findViewById(R.id.progressBar2);
        signIn=findViewById(R.id.google);

        //PRE REQUISITES FOR AUTHENTICATION
        firebaseAuth=FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);

        progressBar.setVisibility(View.INVISIBLE);

        //CHECKS IF USER HAS LOGGED IN VIA GOOGLE ACCOUNT ALREADY
        GoogleSignInAccount signInAccount=GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount !=null){
            callDashboard();
        }

        //GOOGLE SIGN IN BUTTON LISTENER BEGINS
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign=signInClient.getSignInIntent();
                startActivityForResult(sign,GOOGLE_SIGN_IN_CODE);
            }
        });
        //GOOGLE SIGN IN BUTTON LISTENER ENDS

        //CHECKS IF USER HAS ALREADY LOGGED IN USING EMAIL AND PASSWORD
        if(firebaseAuth.getCurrentUser() !=null){
            callDashboard();
        }

        //SIGN IN BUTTON BEGINS
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString().trim();
                String passwordString= password.getText().toString().trim();

                //EMAIL IS REQUIRED
                if(TextUtils.isEmpty(emailString)){
                    email.setError("Email is Required");
                    return;
                }

                //PASSWORD IS REQUIRED
                if(TextUtils.isEmpty(passwordString)){
                    password.setError("Password is Required");
                    return;
                }

                //PASSWORD AT LEAST 6 CHARACTERS
                if(passwordString.length()<6){
                    password.setError("Password must have at least 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LogInScreen.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            finish();
                        }
                        else{
                            Toast.makeText(LogInScreen.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
        //SIGN IN BUTTON ENDS
    }
    //ON CREATE ENDS

    //GOOGLE SIGN IN BEGINS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAcc=signInTask.getResult(ApiException.class);
                Toast.makeText(this, "Google Account is connected", Toast.LENGTH_SHORT).show();
                callDashboard();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    //GOOGLE SIGN IN ENDS

    //GO TO SIGN UP SCREEN BEGINS
    public void signUpScreen(View view) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                if (action.equals("finish_activity")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
        Intent intent = new Intent(LogInScreen.this, SignUpScreen.class);
        startActivity(intent);
    }
    //GO TO SIGN UP SCREEN ENDS
}