package com.project.fttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.project.fttracker.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityLoginBinding binding;
    private TextInputEditText etEmail;
    private TextInputEditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initStuff();

        binding.btnLogin.setOnClickListener(view -> {

            if (Objects.requireNonNull(etEmail.getText()).toString().isEmpty() || Objects.requireNonNull(etPass.getText()).toString().isEmpty()) {
                Toast.makeText(this, "All fields are necessary!", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);
        });

    }

    private void initStuff() {
        auth = FirebaseAuth.getInstance();
        etPass = binding.etPass;
        etEmail = binding.etEmail;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}