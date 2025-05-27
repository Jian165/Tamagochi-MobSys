package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends Fragment implements RecyclerViewInterface {
    View view;
    RecyclerView shopRv;
    ArrayList<String> foodName;
    ArrayList<Integer> foodImage,foodPrice,foodHealth,foodHappiness;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        shopRv = view.findViewById(R.id.shopRecyclerView);
        LoadComponents();
        SetupShopItem();
        LoadItemFromFirebase();
        CheckFoodItem();
        return view;
    }


    private void CheckFoodItem()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(getString(R.string.FOOD_ITEM)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int foodItemCount = queryDocumentSnapshots.size();
                if(foodItemCount!=foodName.size() || queryDocumentSnapshots.isEmpty())
                {
                    SetupShopItem();
                    for(int i=0;i<foodName.size();i++){
                        SaveToFirebase(new ShopItemModel(foodPrice.get(i),foodImage.get(i),foodHealth.get(i),foodHappiness.get(i),foodName.get(i)));
                    }
                }
            }
        });
    }
    private void LoadComponents()
    {
        foodName = new ArrayList<>();
        foodImage= new ArrayList<>();
        foodPrice = new ArrayList<>();
        foodHealth = new ArrayList<>();
        foodHappiness = new ArrayList<>();
    }

    private void UpdateList(){
        ShopRecyclerViewAdopter adopter = new ShopRecyclerViewAdopter(this.getActivity(),ShopItemModel.foodItemList,this);
        shopRv.setAdapter(adopter);
        shopRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    private void SetupShopItem()
    {

        foodImage.add(R.drawable.food_apple);
        foodImage.add(R.drawable.food_cheese);
        foodImage.add(R.drawable.food_burrito);
        foodImage.add(R.drawable.food_burger);
        foodImage.add(R.drawable.food_egg);
        foodImage.add(R.drawable.food_fries);
        foodImage.add(R.drawable.food_ice_cream);
        foodImage.add(R.drawable.food_sushi);
        foodImage.add(R.drawable.food_nuggest);
        foodImage.add(R.drawable.food_pizza);

        foodName.add("Apple");
        foodName.add("Cheese");
        foodName.add("Burrito");
        foodName.add("Burger");
        foodName.add("Egg");
        foodName.add("Fries");
        foodName.add("Ice Cream");
        foodName.add("Sushi");
        foodName.add("Nuggets");
        foodName.add("Pizza");

        foodPrice.add(10);
        foodPrice.add(15);
        foodPrice.add(25);
        foodPrice.add(30);
        foodPrice.add(12);
        foodPrice.add(18);
        foodPrice.add(20);
        foodPrice.add(35);
        foodPrice.add(22);
        foodPrice.add(28);

        foodHealth.add(10);
        foodHealth.add(7);
        foodHealth.add(8);
        foodHealth.add(6);
        foodHealth.add(9);
        foodHealth.add(6);
        foodHealth.add(5);
        foodHealth.add(11);
        foodHealth.add(7);
        foodHealth.add(7);

        foodHappiness.add(2);
        foodHappiness.add(4);
        foodHappiness.add(6);
        foodHappiness.add(7);
        foodHappiness.add(3);
        foodHappiness.add(6);
        foodHappiness.add(8);
        foodHappiness.add(5);
        foodHappiness.add(6);
        foodHappiness.add(7);
    }

    private void LoadItemFromFirebase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(getString(R.string.FOOD_ITEM)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for(DocumentSnapshot doc:documents)
                    {
                        String foodName = doc.getString(getString(R.string.FOOD_NAME));
                        int foodImageId = (doc.getLong(getString(R.string.FOOD_IMAGE))!= null)? doc.getLong(getString(R.string.FOOD_IMAGE)).intValue():0;
                        int foodPrice = (doc.getLong(getString(R.string.FOOD_PRICE))!= null)? doc.getLong(getString(R.string.FOOD_PRICE)).intValue():0;
                        int foodHealth = (doc.getLong(getString(R.string.FOOD_HEALTH))!= null)? doc.getLong(getString(R.string.FOOD_HEALTH)).intValue():0;
                        int foodHappiness = (doc.getLong(getString(R.string.FOOD_HAPPINESS))!= null)? doc.getLong(getString(R.string.FOOD_HAPPINESS)).intValue():0;

                        ShopItemModel.foodItemList.add(new ShopItemModel(foodPrice,foodImageId,foodHealth,foodHappiness,foodName));
                        UpdateList();
                    }
                }
            }
        });
    }



    private void SaveToFirebase(ShopItemModel item)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> FoodInfo = new HashMap<>();
        FoodInfo.put(getString(R.string.FOOD_NAME),item.getItemName());
        FoodInfo.put(getString(R.string.FOOD_IMAGE),item.getItemImage());
        FoodInfo.put(getString(R.string.FOOD_PRICE),item.getItemPrice());
        FoodInfo.put(getString(R.string.FOOD_HEALTH),item.getHealthStat());
        FoodInfo.put(getString(R.string.FOOD_HAPPINESS),item.getHappyStat());

        db.collection(getString(R.string.FOOD_ITEM)).document(item.getItemName()).set(FoodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    System.out.println(item.getItemName()+" Saved");
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent buyIntent= new Intent(this.getActivity(),BuyFood.class);
        buyIntent.putExtra("FOOD_POSITION",position);
        buyIntent.putExtra("CURRENT_MONEY",PetInfoModel.getMoney());
        startActivity(buyIntent);
    }
}