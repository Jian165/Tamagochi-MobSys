package com.example.tamagochi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Shop extends Fragment implements RecyclerViewInterface {
    View view;
    RecyclerView shopRv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        shopRv = view.findViewById(R.id.shopRecyclerView);
        if(ShopItemModel.foodItemList.isEmpty())
        {
            SetupShopItem();
        }
        ShopRecyclerViewAdopter adopter = new ShopRecyclerViewAdopter(this.getActivity(),ShopItemModel.foodItemList,this);
        shopRv.setAdapter(adopter);
        shopRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return view;
    }

    private void SetupShopItem()
    {
        ArrayList<Integer> foodImage= new ArrayList<>();
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

        ArrayList<String> foodName = new ArrayList<>();
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

        ArrayList<Integer> foodPrice = new ArrayList<>();
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

        ArrayList<Integer> foodHealth = new ArrayList<>();
        foodHealth.add(5);
        foodHealth.add(2);
        foodHealth.add(3);
        foodHealth.add(1);
        foodHealth.add(4);
        foodHealth.add(1);
        foodHealth.add(0);
        foodHealth.add(6);
        foodHealth.add(2);
        foodHealth.add(2);

        ArrayList<Integer> foodHappiness = new ArrayList<>();
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

        for(int i=0;i<foodName.size();i++){
            ShopItemModel.foodItemList.add(new ShopItemModel(foodPrice.get(i),foodImage.get(i),foodHealth.get(i),foodHappiness.get(i),foodName.get(i)));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent buyIntent= new Intent(this.getActivity(),BuyFood.class);
        buyIntent.putExtra("FOOD_POSITION",position);
        buyIntent.putExtra("CURRENT_MONEY",PetInfoModel.getMoney());
        startActivity(buyIntent);
    }
}