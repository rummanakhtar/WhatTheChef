package com.example.whatthechef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class Main2Activity extends AppCompatActivity {
    EditText emailsignup,passwordsignup,namesignup,phonesignup;
    Button signupbutton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CountryCodePicker ccp;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        emailsignup=findViewById(R.id.emailsignup);
        passwordsignup=findViewById(R.id.passwordsignup);
        namesignup=findViewById(R.id.namesignup);
        phonesignup=findViewById(R.id.phonesignup);
        signupbutton=findViewById(R.id.signupbutton);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailsignup.getText().toString().trim();
                String password= passwordsignup.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    emailsignup.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordsignup.setError("Password is Required");
                    return;
                }
                if(password.length()<6){
                    passwordsignup.setError("Password must have at least 6 characters");


                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main2Activity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        }else{
                            Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });








    }
}
