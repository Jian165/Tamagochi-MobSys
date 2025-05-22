package com.example.tamagochi;

public class TemDataHandler {
    public static CredentialsModel userLoggedCredentialsModel;
    private static PetInfoModel userLoggedPetInfoModel;

    public static CredentialsModel getUserLoggedCredentialsModel() {
        return userLoggedCredentialsModel;
    }

    public static void setUserLoggedCredentialsModel(CredentialsModel userLoggedCredentialsModel) {
        TemDataHandler.userLoggedCredentialsModel = userLoggedCredentialsModel;
    }

    public static PetInfoModel getUserLoggedPetInfoModel() {
        return userLoggedPetInfoModel;
    }

    public static void setUserLoggedPetInfoModel(PetInfoModel userLoggedPetInfoModel) {
        TemDataHandler.userLoggedPetInfoModel = userLoggedPetInfoModel;
    }
}
