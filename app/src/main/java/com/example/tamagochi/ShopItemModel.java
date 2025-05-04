package com.example.tamagochi;

import java.util.ArrayList;

public class ShopItemModel {


    private int itemPrice,itemImage,healthStat,happyStat;
    public static ArrayList<ShopItemModel> foodItemList = new ArrayList<>();

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemImage() {
        return itemImage;
    }

    public int getHealthStat() {
        return healthStat;
    }

    public int getHappyStat() {
        return happyStat;
    }

    public String getItemName() {
        return itemName;
    }

    String itemName;

    public ShopItemModel(int itemPrice, int itemImage, int healthStat, int happyStat, String itemName) {
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.healthStat = healthStat;
        this.happyStat = happyStat;
        this.itemName = itemName;
    }
}
