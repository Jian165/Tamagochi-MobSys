package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.RecaptchaException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Register extends AppCompatActivity {

    EditText remail,rpassword,confirmPassword;
    Button btnRegister;
    boolean hasError;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        HideSystemUI();
        LoadComponents();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hasError = false;
                String registerUsername =  remail.getText().toString();
                String registerPassword =  rpassword.getText().toString();
                String registerConfirmPassword =  confirmPassword.getText().toString();

               if(registerUsername.isEmpty()){
                   remail.setError("Enter a username");
                   hasError = true;
               }

                if(registerPassword.isEmpty()){
                    rpassword.setError("Enter a password");
                    hasError = true;
                }
                if(registerConfirmPassword.isEmpty()){
                    confirmPassword.setError("Confirm your password");
                    hasError = true;
                }

                if(!registerConfirmPassword.equals(rpassword.getText().toString())){
                    confirmPassword.setError("Password doesn't match");
                    hasError=true;
                }

                if(registerPassword.length() < 6)
                {
                    rpassword.setError("Password too Short");
                   hasError = true;
                }

                if (!hasError){
                    RegisterUser(remail.getText().toString(),rpassword.getText().toString());
                }
            }
        });

    }

    private void RegisterUser(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register.this, "You Are now Registered", Toast.LENGTH_SHORT).show();
                    Intent backtoLoginIntent = new Intent(Register.this,MainActivity.class);
                    startActivity(backtoLoginIntent);
                    finish();
                }
                else
                {
                    Exception e = task.getException();
                    if(e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        remail.setError("Invalid Email");
                    }
                    if(e instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(Register.this, "Email Already used", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(Register.this, "Register Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadComponents()
    {
        auth = FirebaseAuth.getInstance();
        remail = findViewById(R.id.rEmailEdt);
         rpassword = findViewById(R.id.rPasswordEdt);
         confirmPassword = findViewById(R.id.rConfirmPasswordEdt);
         btnRegister = findViewById(R.id.registerUserButton);
    }
    private void HideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}