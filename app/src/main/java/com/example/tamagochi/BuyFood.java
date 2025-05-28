package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class BuyFood extends AppCompatActivity {

    TextView foodNameLbl, currentMoneyLbl,foodPriceLbl,totalPriceLbl,healthStatLbl,happyStatLbl;
    EditText foodQuantity;
    ImageView foodImg;
    Button payBtn;
    String foodName;
    int foodPositonList, foodImage,quantity,totalPrice,foodPrice,currentMoney, foodhealthStat,foodhappyStat;
    ShopItemModel shopItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LoadComponents();

        currentMoney = getIntent().getIntExtra("CURRENT_MONEY",0);
        foodPositonList = getIntent().getIntExtra("FOOD_POSITION",0);
        shopItem = ShopItemModel.foodItemList.get(foodPositonList);

        foodName = shopItem.getItemName();
        foodPrice = shopItem.getItemPrice();
        foodImg.setImageResource(shopItem.getItemImage());
        foodhealthStat = shopItem.getHealthStat();
        foodhappyStat = shopItem.getHealthStat();

        currentMoneyLbl.setText("$"+currentMoney);
        foodPriceLbl.setText("$"+foodPrice);
        foodNameLbl.setText(foodName);
        healthStatLbl.setText("+"+foodhealthStat);
        happyStatLbl.setText("+"+foodhealthStat);



        foodQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!foodQuantity.getText().toString().isEmpty() || !foodQuantity.getText().toString().isBlank()){
                    quantity = Integer.parseInt(foodQuantity.getText().toString());
                    totalPrice = CalculateTotalPrice(foodPrice,quantity);
                    totalPriceLbl.setText("$"+totalPrice);
                }
                else{
                    totalPriceLbl.setText("$"+0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasQuantity = true;
                if(foodQuantity.getText().toString().isEmpty() || foodQuantity.getText().toString().isBlank())
                {
                   hasQuantity = false;
                   foodQuantity.setError("Enter Quantity");
                }
                if(PetInfoModel.getMoney()>=totalPrice && hasQuantity){
                    Toast.makeText(BuyFood.this, "Successfully Purchase: "+foodName, Toast.LENGTH_SHORT).show();
                    StoreFoodPurchased(shopItem,quantity);
                    Intent backToDashboard = new Intent(BuyFood.this,Dashboard.class);
                    MakePayment(totalPrice);
                    startActivity(backToDashboard);
                    finish();
                }
                else
                {
                    Toast.makeText(BuyFood.this, "Sorry not enough funds", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void MakePayment(int totalPrice)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists())
                    {
                        docRef.update(getString(R.string.MONEY),FieldValue.increment(-totalPrice));
                    }
                }
            }
        });
    }

    private void StoreFoodPurchased(ShopItemModel itemToStore, int quantity){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef =  db.collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists())
                    {
                        Map<String, Object> foodItems = (Map<String, Object>) doc.get(getString(R.string.FOOD_STORAGE));
                        if(foodItems != null && foodItems.containsKey(itemToStore.getItemName()))
                        {
                            UpdateItemQuantity(itemToStore,quantity,docRef);
                        }
                        else
                        {
                            AddItemToList(itemToStore,quantity,docRef);
                        }
                    }
                }

            }
        });
    }
    private void AddItemToList(ShopItemModel item,int itemQuantity,DocumentReference docRef)
    {

        Map<String,Object> stats = new HashMap<>();
        stats.put(getString(R.string.FOOD_IMAGE),item.getItemImage());
        stats.put(getString(R.string.FOOD_HEALTH),item.getHealthStat());
        stats.put(getString(R.string.FOOD_HAPPINESS),item.getHappyStat());

        Map<String, Object> foodData = new HashMap<>();
        foodData.put(getString(R.string.FOOD_ITEM_STATS),stats);
        foodData.put(getString(R.string.FOOD_QUANTITY),itemQuantity);

        Map<String,Object> foodItem = new HashMap<>();
        foodItem.put(item.getItemName(),foodData);

        Map<String,Object> foodItemFiled = new HashMap<>();
        foodItemFiled.put(getString(R.string.FOOD_STORAGE),foodItem);

        docRef.set(foodItemFiled, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
    }

    private void UpdateItemQuantity(ShopItemModel item, int quantityToAdd, DocumentReference docRef)
    {
        String fieldPath = getString(R.string.FOOD_STORAGE)+"."+item.getItemName()+"."+getString(R.string.FOOD_QUANTITY);
        docRef.update(fieldPath, FieldValue.increment(quantityToAdd)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("BuyFood: add food quantity successfully");
            }
        });
    }


    private int CalculateTotalPrice(int itemPrice, int itemQuantity)
    {
        return itemPrice * itemQuantity;
    }


    private void LoadComponents()
    {
       foodNameLbl = findViewById(R.id.lblFoodOrderName);
        currentMoneyLbl = findViewById(R.id.lblOrderCurrentMoney);
        foodPriceLbl = findViewById(R.id.lblFoodOrderPice);
        totalPriceLbl = findViewById(R.id.lblTotalPrice);
        foodQuantity = findViewById(R.id.txtOrderQuantity);
        foodImg = findViewById(R.id.imgfoodOrderImage);
        payBtn =  findViewById(R.id.btnPay);
        healthStatLbl = findViewById(R.id.lblFoodBuyHealthStat);
        happyStatLbl = findViewById(R.id.lblFoodBuyHealthStat);
    }
}