package com.example.tamagochi;

import java.util.HashMap;

public class PetInfoModel {
    public PetInfoModel(String parentName, String petName, String petType, int money, int health, int happy, boolean isAlive) {
        this.parentName = parentName;
        this.petName = petName;
        this.petType = petType;
        this.money = money;
        this.health = health;
        this.happy = happy;
        this.isAlive = isAlive;
    }

    private String parentName;
    private  String petName;
    private  String petType ;
    private  int money;
    private  int health;
    private  int happy;
    private  int clean;
    private  boolean isAlive;

    public static HashMap<ShopItemModel,Integer> foodStuck = new HashMap<>();
    public static HashMap<Integer,Boolean> shitCollection= new HashMap<>();


    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
