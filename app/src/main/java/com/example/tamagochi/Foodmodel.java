package com.example.tamagochi;

import java.util.ArrayList;

public class Foodmodel {
    private static String foodName;

    public static String getFoodName() {
        return foodName;
    }

    public static void setFoodName(String foodName) {
        Foodmodel.foodName = foodName;
    }

    public static int getHealth() {
        return health;
    }

    public static void setHealth(int health) {
        Foodmodel.health = health;
    }

    public static int getHappy() {
        return happy;
    }

    public static void setHappy(int happy) {
        Foodmodel.happy = happy;
    }

    public static int getImgId() {
        return ImgId;
    }

    public static void setImgId(int imgId) {
        ImgId = imgId;
    }

    public static int getQuantity() {
        return Quantity;
    }

    public static void setQuantity(int quantity) {
        Quantity = quantity;
    }

    private static int health,happy,ImgId,Quantity;
}
