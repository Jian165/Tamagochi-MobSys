package com.example.tamagochi;


public class CredentialsModel {
    public  static String getCurrentUserUDI() {
        return currentUserUDI;
    }

    public static void setCurrentUserUDI(String currentUserUDI) {
        CredentialsModel.currentUserUDI = currentUserUDI;
    }

    public static String getPetDI() {
        return petDI;
    }

    public static void setPetDI(String petDI) {
        CredentialsModel.petDI = petDI;
    }

    private static String currentUserUDI;
    private static String petDI;

}