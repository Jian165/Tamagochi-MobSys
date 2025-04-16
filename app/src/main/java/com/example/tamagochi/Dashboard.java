package com.example.tamagochi;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    ImageView characterImg;
    private Runnable runnable;
    private ArrayList<Integer> charachterIdleImages;
    private int IdleImgIndex = 0;
    private Handler handler =  new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        charachterIdleImages = new ArrayList<>();
        charachterIdleImages.add(R.drawable.idle_1);
        charachterIdleImages.add(R.drawable.idle_2);
        charachterIdleImages.add(R.drawable.idle_3);
        charachterIdleImages.add(R.drawable.idle_4);
        charachterIdleImages.add(R.drawable.idle_5);
        charachterIdleImages.add(R.drawable.idle_6);
        charachterIdleImages.add(R.drawable.idle_7);
        charachterIdleImages.add(R.drawable.idle_8);
        charachterIdleImages.add(R.drawable.idle_9);
        charachterIdleImages.add(R.drawable.idle_10);

        characterImg = findViewById(R.id.imgCharacter);
        runnable = new Runnable() {
            @Override
            public void run() {
                characterImg.setImageResource(charachterIdleImages.get(IdleImgIndex));
                IdleImgIndex = (IdleImgIndex+1)%charachterIdleImages.size();
                handler.postDelayed(this,60);
            }
        };
        handler.post(runnable);

    }

    private void PlayIdle()
    {
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


}