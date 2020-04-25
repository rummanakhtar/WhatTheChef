package com.example.whatthechef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;


public class Main2Activity extends AppCompatActivity {


    EditText emailsignup,passwordsignup,namesignup,phonesignup;
    Button signupbutton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        final CountryCodePicker countryCodePicker;
        countryCodePicker=findViewById(R.id.ccp);

        emailsignup=findViewById(R.id.emailsignup);
        passwordsignup=findViewById(R.id.passwordsignup);
        namesignup=findViewById(R.id.namesignup);
        phonesignup=findViewById(R.id.phonesignup);
        signupbutton=findViewById(R.id.signupbutton);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailsignup.getText().toString().trim();
                String password= passwordsignup.getText().toString().trim();
                final String name=namesignup.getText().toString().trim();
                final String phone=phonesignup.getText().toString().trim();
                final String countrycode=countryCodePicker.getSelectedCountryCode().toString().trim();

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
                            userID=firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("Name",name);
                            user.put("Phone",phone);
                            user.put("Email",email);
                            user.put("Country Code","0"+countrycode);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("TAG","User profile created for user ID: "+userID);
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            finish();
                            Intent intent = new Intent("finish_activity");
                            sendBroadcast(intent);


                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });








    }
}
