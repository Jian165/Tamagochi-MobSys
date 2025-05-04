package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText rusername,rpassword,confirmPassword;
    Button btnRegister;
    boolean hasError;

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
               if(rusername.getText().toString().isEmpty()){
                   rusername.setError("Enter a username");
                   hasError = true;
               }

                if(rpassword.getText().toString().isEmpty()){
                    rpassword.setError("Enter a password");
                    hasError = true;
                }
                if(confirmPassword.getText().toString().isEmpty()){
                    confirmPassword.setError("Confirm your password");
                    hasError = true;
                }

                if(!confirmPassword.getText().toString().equals(rpassword.getText().toString())){
                    confirmPassword.setError("Password doesn't match");
                    hasError=true;
                }

                if (!hasError){
                    Toast.makeText(Register.this, "You Are now Registered", Toast.LENGTH_SHORT).show();
                    CridentialsModel.setPassword(rpassword.getText().toString());
                    CridentialsModel.setUsername(rusername.getText().toString());
                    Intent backtoLoginIntent = new Intent(Register.this,MainActivity.class);
                    startActivity(backtoLoginIntent);
                }
            }
        });

    }

    private void LoadComponents()
    {
        rusername = findViewById(R.id.rUsernameEdt);
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