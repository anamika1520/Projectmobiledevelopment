package com.project.fttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.fttracker.databinding.ActivitySignUpBinding;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivitySignUpBinding binding;
    private TextInputEditText etEmail;
    private TextInputEditText etFname;
    private TextInputEditText etLname;
    private TextInputEditText etPass;
    private TextInputEditText etCPass;
    private TextInputLayout tilCPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initStuff();

        binding.btnSignUpACT.setOnClickListener(view -> {
            if(Objects.requireNonNull(etEmail.getText()).toString().equals("")
                    || Objects.requireNonNull(etFname.getText()).toString().equals("")

                    || Objects.requireNonNull(etLname.getText()).toString().equals("")

                    || Objects.requireNonNull(etPass.getText()).toString().equals("")

                    || Objects.requireNonNull(etCPass.getText()).toString().equals("")){

                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }else{
                if(etPass.getText().toString().equals(etCPass.getText().toString())){
                    auth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Created account successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                task.addOnFailureListener(e -> {
                                    Toast.makeText(SignUpActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                            }
                        }
                    });
                }else{
                    tilCPass.setError("Password don't match");
                }
            }
        });

    }

    private void initStuff() {
        auth = FirebaseAuth.getInstance();
        etPass = binding.etPass;
        etEmail = binding.etEmailSignUp;
        etFname = binding.etFName;
        etLname = binding.etLName;
        etCPass = binding.etCPass;
        tilCPass = binding.tilCPass;
    }
}