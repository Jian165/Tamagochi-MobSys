package com.example.tamagochi;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class PetInfoModel {
    private static String ParentName;
    private static String PetName;
    private static boolean isCat;
    private static int money = 0;
    private static int health = 0;
    private static int happy = 0;
    private static int clean = 0;

    public static int getClean() {
        return clean;
    }

    public static void setClean(int clean) {
        PetInfoModel.clean = clean;
    }

    public static boolean isIsAlive() {
        return isAlive;
    }

    public static void setIsAlive(boolean isAlive) {
        PetInfoModel.isAlive = isAlive;
    }

    private static boolean isAlive;

    public static HashMap<ShopItemModel,Integer> foodStuck = new HashMap<>();
    public static HashMap<Integer,Boolean> shitCollection= new HashMap<>();

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

    public static boolean isIsCat() {
        return isCat;
    }

    public static void setIsCat(boolean isCat) {
        PetInfoModel.isCat = isCat;
    }

    public static String getPetName() {
        return PetName;
    }

    public static void setPetName(String petName) {
        PetName = petName;
    }

    public static String getParentName() {
        return ParentName;
    }

    public static void setParentName(String parentName) {
        ParentName = parentName;
    }

}
