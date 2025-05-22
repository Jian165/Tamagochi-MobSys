package com.example.tamagochi;


import com.google.firebase.auth.FirebaseUser;

public class CredentialsModel {
    public  String getCurrentUserUDI() {
        return currentUserUDI;
    }

    public void setCurrentUserUDI(String currentUserUDI) {
        this.currentUserUDI = currentUserUDI;
    }

    public String getPetDI() {
        return petDI;
    }

    public void setPetDI(String petDI) {
        this.petDI = petDI;
    }

    private String currentUserUDI;
    private String petDI;

}