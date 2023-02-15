package com.example.strive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityThree extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.button2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityThree.this, ActivityFour.class));
                finish();
            }
        });
    }
}