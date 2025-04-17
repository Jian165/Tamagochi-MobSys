package com.example.tamagochi;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    ImageView characterImg,shit1Img,shit2Img,shit3Img,shit4Img,shit5Img,cleanImg;
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
        hideSystemUI();
        LoadComponents();
        PlayIdle();

        cleanImg.setOnLongClickListener(itemClickListiner());
        shit1Img.setOnDragListener(shitDragListiner(shit1Img));
        shit2Img.setOnDragListener(shitDragListiner(shit2Img));
        shit3Img.setOnDragListener(shitDragListiner(shit3Img));
        shit4Img.setOnDragListener(shitDragListiner(shit4Img));
        shit5Img.setOnDragListener(shitDragListiner(shit5Img));
    }

    private View.OnLongClickListener itemClickListiner(){
        return  new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data,shadowBuilder,v,0);
                return true;
            }
        };

    }

    private View.OnDragListener shitDragListiner(ImageView shitImage){
       return new View.OnDragListener() {
           @Override
           public boolean onDrag(View v, DragEvent event) {
               int dragEvent =  event.getAction();
               switch (dragEvent){
                   case DragEvent.ACTION_DRAG_ENTERED:
                       break;
                   case DragEvent.ACTION_DRAG_EXITED:
                       break;
                   case DragEvent.ACTION_DROP:
                       final View view = (View) event.getLocalState();
                       if(view.getId() == R.id.clean) {
                           shitImage.setAlpha(0f);
                       }
                       break;
               }
               return true;
           }
       };
    }
    private void LoadComponents(){
        characterImg = findViewById(R.id.imgCharacter);
        cleanImg = findViewById(R.id.clean);
        shit1Img = findViewById(R.id.imgShit1);
        shit2Img = findViewById(R.id.imgShit2);
        shit3Img = findViewById(R.id.imgShit3);
        shit4Img = findViewById(R.id.imgShit4);
        shit5Img = findViewById(R.id.imgShit5);
    }

    private void PlayIdle()
    {
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
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


}