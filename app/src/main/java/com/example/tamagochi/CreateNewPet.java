package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateNewPet extends AppCompatActivity {
    EditText petName, parentName;
    Button createPet;
    RadioGroup radioCharacter;
    RadioButton radioDog,radioCat;
    boolean hasError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_pet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LoadComponents();
        radioCat.setChecked(true);

        createPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasError = false;
               if(parentName.getText().toString().isEmpty()){
                   parentName.setError("Enter Your Name");
                   hasError = true;
               }
                if(petName.getText().toString().isEmpty()){
                    petName.setError("Enter Your Pets Name");
                    hasError = true;
                }

                if (!hasError){
                    PetInfoModel.setPetName(petName.getText().toString());
                    PetInfoModel.setParentName(parentName.getText().toString());
                    PetInfoModel.setHealth(20);
                    PetInfoModel.setHappy(30);
                    PetInfoModel.setMoney(0);
                    int selectedID = radioCharacter.getCheckedRadioButtonId();
                    if(selectedID == R.id.radioCat){
                        PetInfoModel.setIsCat(true);
                    } else if (selectedID == R.id.radioDog) {
                        PetInfoModel.setIsCat(false);
                    }
                    Intent intent = new Intent(CreateNewPet.this, Dashboard.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    private void LoadComponents(){
        petName = findViewById(R.id.edtPetName);
        parentName = findViewById(R.id.edtParentName);
        createPet = findViewById(R.id.btnCreatePet);
        radioCat = findViewById(R.id.radioCat);
        radioDog = findViewById(R.id.radioDog);

        radioCharacter = findViewById(R.id.rgCharacterSelect);
    }
}