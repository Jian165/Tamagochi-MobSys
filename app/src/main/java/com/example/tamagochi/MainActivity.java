package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button LoginButton, RegisterButton;
    EditText usernameTxt,passwordTxt;
    CheckBox passwordVisibility;
    boolean hasError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadComponents();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasError=false;
                if (usernameTxt.getText().toString().isEmpty()){
                    usernameTxt.setError("Enter Username");
                    hasError = true;
                }
                if (passwordTxt.getText().toString().isEmpty()){
                    passwordTxt.setError("Enter Username");
                    hasError = true;
                }
                if((!usernameTxt.getText().toString().equals(CridentialsModel.getUsername())||(!passwordTxt.getText().toString().equals(CridentialsModel.getPassword())))){
                    System.out.println(CridentialsModel.getUsername());
                    System.out.println(CridentialsModel.getPassword());
                   usernameTxt.setError("User and Password doesn't match");
                   passwordTxt.setError("User and Password doesn't match");
                   hasError = true;
                }
                if(!hasError){
                   Intent intent = new Intent(MainActivity.this, CreateNewPet.class);
                   intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                }
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Register.class);
                startActivity(registerIntent);
            }
        });

        passwordVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    passwordTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    passwordTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        hideSystemUI();

    }

    private void loadComponents()
    {
        LoginButton = findViewById(R.id.loginButton);
        RegisterButton = findViewById(R.id.registerButton);
        usernameTxt = findViewById(R.id.usernameEdt);
        passwordTxt = findViewById(R.id.passwordEdt);
        passwordVisibility = findViewById(R.id.cbPasswordVisibility);
    }
    private void hideSystemUI() {
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