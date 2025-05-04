package com.example.tamagochi;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Fragment {
    TextView profilePetName,parentName;
    static TextView currentMoney;
    ImageView petProfileImg;
    Button BtnLogout;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        LoadComponents();

        profilePetName.setText(PetInfoModel.getPetName().toString());
        parentName.setText(PetInfoModel.getParentName().toString());
        if(PetInfoModel.isIsCat()){
            petProfileImg.setImageResource(R.drawable.idle_1);
        }
        else {
            petProfileImg.setImageResource(R.drawable.dog_idle1);
        }
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        Profile.updateCurrentMoney();
        return view;
    }
    public static void updateCurrentMoney(){
        try{
            currentMoney.setText("$"+PetInfoModel.getMoney());
        }
        catch (Exception e)
        {
            System.out.println(PetInfoModel.getMoney());
        }
    }
    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out and close the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(),MediaPlayback.class);
                getActivity().stopService(intent);
                finishAffinity(getActivity());
                System.exit(0);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Logout cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }
    private void LoadComponents()
    {
        profilePetName =view.findViewById(R.id.lblProfilePetName);
        parentName = view.findViewById(R.id.lblProfileParentName);
        currentMoney = view.findViewById(R.id.lblProfileCurrentMoney);
        petProfileImg = view.findViewById(R.id.imgProfileCharacter);
        BtnLogout = view.findViewById(R.id.btnLogout);
    }
}