package com.example.strive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityTwo extends AppCompatActivity {
    EditText phone, otp;
    Button btngenerateOTP;
    FirebaseAuth mAuth;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        phone = findViewById(R.id.editTextPhone);
        otp = findViewById(R.id.editTextNumberPassword);
        btngenerateOTP = findViewById(R.id.button_otp);
        mAuth = FirebaseAuth.getInstance();

        btngenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(ActivityTwo.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String number = phone.getText().toString();
                    sendverificationcode(number);
                }
            }
        });

    }

    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        public void onVerificationFailed(@NonNull FirebaseException e, String s) {
            Toast.makeText(ActivityTwo.this, "Verification Failed!", Toast.LENGTH_SHORT).show();


    };

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
        }
        private void verifycode(String code){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
            signinbyCredentials(credential);
        }
    };

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ActivityTwo.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityTwo.this, ActivityThree.class));
                        }
                    }
                });
    }
}