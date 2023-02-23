package com.example.strive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityTwo extends AppCompatActivity {
    EditText phone, otp;
    Button btnGenerateOTP;
    FirebaseAuth mAuth;
    String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        phone = findViewById(R.id.editTextPhone);
        otp = findViewById(R.id.editTextNumberPassword);
        btnGenerateOTP = findViewById(R.id.button_otp);
        mAuth = FirebaseAuth.getInstance();

        btnGenerateOTP.setOnClickListener(view -> {
            if(TextUtils.isEmpty(phone.getText().toString())){
                Toast.makeText(ActivityTwo.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
            }
            else {
                String number = phone.getText().toString();
                sendVerificationCode(number);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }



    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ActivityTwo.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }

        /*@Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }*/

        /*public void onVerificationFailed(@NonNull FirebaseException e, String s) {
            Toast.makeText(ActivityTwo.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }*/

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
        }

        
        private void verifyCode(String code){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
            signInByCredentials(credential);
        }
    };

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ActivityTwo.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityTwo.this, ActivityThree.class));
                    }
                });
    }
}