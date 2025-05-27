package com.example.tamagochi;

import java.util.HashMap;
import java.util.Map;

public class PetInfoModel {
    private static String parentName;
    private static String petName;
    private static String petType ;
    private static  int money;
    private static int health;
    private static int happy;
    private static int clean;
    private static boolean isAlive;

    public static HashMap<ShopItemModel,Integer> foodStuck = new HashMap<>();
    public static HashMap<Integer,Boolean> shitCollection= new HashMap<>();


    public static boolean isIsAlive() {
        return isAlive;
    }

    public static void setIsAlive(boolean isAlive) {
        PetInfoModel.isAlive = isAlive;
    }


    public static int getHealth() {
        return health;
    }

    public static void setHealth(int health) {
        PetInfoModel.health = health;
    }

    public static int getHappy() {
        return happy;
    }

    public static void setHappy(int happy) {
        PetInfoModel.happy = happy;
    }

    public static int getMoney() {
        return money;
    }

    public static void setMoney(int money) {
        PetInfoModel.money = money;
    }

    public static String getPetType() {
        return petType;
    }

    public static void setPetType(String petType) {
        PetInfoModel.petType = petType;
    }

    public static String getPetName() {
        return petName;
    }

    public static void setPetName(String petName) {
        PetInfoModel.petName = petName;
    }

    public static String getParentName() {
        return parentName;
    }

    public static void setParentName(String parentName) {
        PetInfoModel.parentName = parentName;
    }

}
