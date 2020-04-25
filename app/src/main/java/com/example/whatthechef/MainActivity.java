package com.example.whatthechef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
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

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button signinbutton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private long backPressedTime;
    private Toast backToast;


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
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.emailsignin);
        password=findViewById(R.id.passwordsignin);
        signinbutton=findViewById(R.id.signinbutton);
        progressBar=findViewById(R.id.progressBar2);


        firebaseAuth=FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        //check if user has logged in previously using email and password
        if(firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring = email.getText().toString().trim();
                String passwordstring= password.getText().toString().trim();
                if(TextUtils.isEmpty(emailstring)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(passwordstring)){
                    password.setError("Password is Required");
                    return;
                }
                if(passwordstring.length()<6){
                    password.setError("Password must have at least 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(emailstring,passwordstring).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }

    public void signupscreen(View view) {
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
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);

    }
    public void dashboard(View view) {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
    }


}
