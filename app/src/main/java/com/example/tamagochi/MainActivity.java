package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity{

    Button LoginButton;
    ImageButton googleSignIn, emailSignIn;
    EditText usernameTxt,passwordTxt;
    CheckBox passwordVisibility;
    boolean hasError;
    private FirebaseAuth auth;
    static boolean isUserHasRecord;
    private static CredentialsModel userLoggedCredentials;
    Intent createPetActivity, dashboardActivity;

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
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if (username.isEmpty()){
                    usernameTxt.setError("Enter Username");
                    hasError = true;
                }
                if (password.isEmpty()){
                    passwordTxt.setError("Enter Username");
                    hasError = true;
                }
                if(!hasError){
                    LoginUser(username,password);
                }
            }
        });


        emailSignIn.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            userLoggedCredentials.setCurrentUserUDI(FirebaseAuth.getInstance().getCurrentUser().getUid());
            TemDataHandler.setUserLoggedCredentialsModel(userLoggedCredentials);
            isUserInList(TemDataHandler.getUserLoggedCredentialsModel().getCurrentUserUDI());
        }
    }

    private void LoginUser(String username, String password)
    {
        auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                userLoggedCredentials.setCurrentUserUDI(FirebaseAuth.getInstance().getCurrentUser().getUid());
                TemDataHandler.setUserLoggedCredentialsModel(userLoggedCredentials);
                isUserInList(TemDataHandler.getUserLoggedCredentialsModel().getCurrentUserUDI());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    usernameTxt.setError("Invalid Email");
                }
                Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void loadComponents()
    {
        createPetActivity = new Intent(MainActivity.this, CreateNewPet.class);
        dashboardActivity = new Intent(MainActivity.this, Dashboard.class);
        auth =  FirebaseAuth.getInstance();
        LoginButton = findViewById(R.id.loginButton);
        usernameTxt = findViewById(R.id.emialEdt);
        passwordTxt = findViewById(R.id.passwordEdt);
        passwordVisibility = findViewById(R.id.cbPasswordVisibility);
        googleSignIn = findViewById(R.id.signInWithGoogleBtn);
        emailSignIn = findViewById(R.id.signInWithEmailBtn);

        userLoggedCredentials = new CredentialsModel();

    }
    private void isUserInList(String UserID)
    {
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo(FieldPath.documentId(),UserID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    startActivity(dashboardActivity);
                    dashboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                else
                {
                    startActivity(createPetActivity);
                    createPetActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }

            }
        });
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