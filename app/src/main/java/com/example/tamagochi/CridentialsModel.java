package com.example.tamagochi;

public class CridentialsModel {
    private static String username;

    public static void setPassword(String password) {
        CridentialsModel.password = password;
    }

    public static void setUsername(String username) {
        CridentialsModel.username = username;
    }

    private static String password;


    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

}
