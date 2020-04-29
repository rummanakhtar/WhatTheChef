package com.example.whatthechef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import java.util.Objects;


public class Main2Activity extends AppCompatActivity {
    EditText emailSignUp,passwordSignUp,nameSignUp,phoneSignUp;
    Button signUpButton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final CountryCodePicker countryCodePicker;

        //LINKING THE XML ELEMENTS WITH THEIR RESPECTIVE JAVA VARIABLES
        countryCodePicker=findViewById(R.id.ccp);
        emailSignUp=findViewById(R.id.emailsignup);
        passwordSignUp=findViewById(R.id.passwordsignup);
        nameSignUp=findViewById(R.id.namesignup);
        phoneSignUp=findViewById(R.id.phonesignup);
        signUpButton=findViewById(R.id.signupbutton);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //SIGN UP BUTTON BEGINS
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailSignUp.getText().toString().trim();
                String password= passwordSignUp.getText().toString().trim();
                final String name=nameSignUp.getText().toString().trim();
                final String phone=phoneSignUp.getText().toString().trim();
                final String countryCode=countryCodePicker.getSelectedCountryCode().toString().trim();

                if(TextUtils.isEmpty(name)){
                    nameSignUp.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    phoneSignUp.setError("Phone number is Required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    emailSignUp.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    passwordSignUp.setError("Password is Required");
                    return;
                }
                if(password.length()<6){
                    passwordSignUp.setError("Password must have at least 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main2Activity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            userID= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            DocumentReference documentReference=firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("Name",name);
                            user.put("Phone",phone);
                            user.put("Email",email);
                            user.put("Country Code","0"+countryCode);
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
                            Toast.makeText(Main2Activity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //SIGN UP BUTTON ENDS
    }
    //ON CREATE ENDS
}
