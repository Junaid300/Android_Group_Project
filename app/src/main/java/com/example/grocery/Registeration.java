package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class Registeration extends AppCompatActivity {
EditText mFullName,mEmail,mPassword,mPhoneNumber;
Button mRegisterBtn;
TextView mLoginBtn;
FirebaseAuth fAuth;
ProgressBar progressBar;
    FirebaseFirestore fStore;
String useId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
    mFullName= findViewById(R.id.fullName);
    mEmail=findViewById(R.id.login_email);
    mPassword=findViewById(R.id.login_password);
    mPhoneNumber=findViewById(R.id.phonenumber);
    mRegisterBtn=findViewById(R.id.loginBtn);
    mLoginBtn=findViewById(R.id.loginhere);

    fAuth=FirebaseAuth.getInstance();
    fStore=FirebaseFirestore.getInstance();
    progressBar=findViewById(R.id.progressBar);
    if(fAuth.getCurrentUser() !=null)
    {
        Toast.makeText(Registeration.this, "Verfication Has been Send",Toast.LENGTH_SHORT).show();
    finish();
    }

    mRegisterBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email=mEmail.getText().toString().trim();
            String password=mPassword.getText().toString().trim();
            String fullName=mFullName.getText().toString().trim();
            String phoneNumber=mPhoneNumber.getText().toString().trim();

            if(TextUtils.isEmpty(fullName))
            {
                mFullName.setError("Full Name is Required");
                return ;
            }
            if(TextUtils.isEmpty(email))
    {
        mEmail.setError("Email is Required");
        return ;
    }

            if(TextUtils.isEmpty(phoneNumber))
            {
                mPhoneNumber.setError("Phone is Required");
                return ;
            }
            if(TextUtils.isEmpty(password))
            {
                mPassword.setError("Password is Required");
                return ;
            }
            if(password.length()<6)
            {
                mPassword.setError("Password Should have Atleast 6 Characters");
                return ;
            }
            progressBar.setVisibility((View.VISIBLE));

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
//                       Toast.makeText(Registeration.this, "Registered SuccessFully",Toast.LENGTH_SHORT).show();
                    useId=fAuth.getCurrentUser().getUid();
                       DocumentReference documentReference=fStore.collection("UsersData").document(useId);
                       Map<String,Object>user=new HashMap<>();
                       user.put("FullName",fullName);
                       user.put("Email",email);
                       user.put("PhoneNumber",phoneNumber);
                       user.put("password",password);
                       documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(Registeration.this, "USER CREATED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(Registeration.this, "Fail To create User...",Toast.LENGTH_SHORT).show();
                           }
                       });;
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                   }
                   else
                   {
                       Toast.makeText(Registeration.this, "Error!.."+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.GONE);
                   }
                }
            });
        }
    });
    mLoginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
    });
    }
}