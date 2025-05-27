package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
                    int selectedID = radioCharacter.getCheckedRadioButtonId();
                    String nameOfPet = petName.getText().toString();
                    String nameOfParent = parentName.getText().toString();
                    String petType = null;
                    if(selectedID == R.id.radioCat){
                        petType = "Cat";
                    } else if (selectedID == R.id.radioDog) {
                        petType = "Dog";
                    }

                    CreatePet(nameOfParent,nameOfPet,petType);


                    Intent intent = new Intent(CreateNewPet.this, Dashboard.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void CreatePet(String parentName,String petName,String petType)
    {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        String PetID = db.collection(getString(R.string.PETS)).document().getId();
        Map<String,Object> PetOwnerInfo = new HashMap<>();
        PetOwnerInfo.put(getString(R.string.PET_OWNED),PetID);
        PetOwnerInfo.put(getString(R.string.NAME_AS_PARENT),parentName);

        db.collection(getString(R.string.USERS)).document(CredentialsModel.getCurrentUserUDI()).set(PetOwnerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateNewPet.this, "Welcome", Toast.LENGTH_SHORT).show();
            }
        });

        Map<String,Object> FoodStorage = new HashMap<>();
        Map<String,Object> PoopStorage = new HashMap<>();

        Map<String,Object> PetInfo = new HashMap<>();
        PetInfo.put(getString(R.string.PET_NAME),petName);
        PetInfo.put(getString(R.string.PET_GENDER),petType);
        PetInfo.put(getString(R.string.IS_ALIVE),true);
        PetInfo.put(getString(R.string.LAST_FED), Timestamp.now());
        PetInfo.put(getString(R.string.HEALTH),80);
        PetInfo.put(getString(R.string.HAPPINESS),80);
        PetInfo.put(getString(R.string.MONEY),2);
        PetInfo.put(getString(R.string.POOP),PoopStorage);
        PetInfo.put(getString(R.string.FOOD_STORAGE),FoodStorage);

        db.collection(getString(R.string.PETS)).document(PetID).set(PetInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(CreateNewPet.this, "Your Pet is Now Created", Toast.LENGTH_SHORT).show();
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