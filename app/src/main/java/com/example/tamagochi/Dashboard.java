package com.example.tamagochi;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private ImageView characterImg, shit1Img, shit2Img, shit3Img, shit4Img, shit5Img, feedImg, cleanImg,shadowImg,playImg,playballImg;
    private ProgressBar healthProg, happinessProg;
    private FrameLayout playBallArea;

    private float velocity = 0, gravity = 0.7f, jumpStrangth = -16f;
    private int screenHeight;
    private boolean isIdle = false, isPlayBallIn = false;
    private Runnable runnable,ballGameLoop;
    private ArrayList<Integer> charachterIdleImages,characterDeadImages,catIdleImages,catDeadImages,dogIdleImages,dogDeadImages,shitCollected;
    private int idleImgIndex = 0,deadImgIndex = 0,eatingImg;

    private boolean isCharacterAlive;

    private boolean isCat=false;

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
        HideSystemUI();
        LoadComponents();
        CheckHealth();
        CheckCharacter();
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
                       if(view.getId() == R.id.imgClean) {
                           if(!shitCollected.contains(shitImage.getId())){
                               shitCollected.add(shitImage.getId());
                           }
                           shitImage.setAlpha(0f);
                       }
                       break;
               }
               return true;
           }
       };
    }
    private View.OnTouchListener BallAreaListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    velocity=jumpStrangth;
                    if(isCharacterAlive){
                        HappinessIncrease(1*shitCollected.size());
                    }
                }
                return true;
            }
        };
    }
    private View.OnDragListener CharacterListener(ImageView characterImg){
        return new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent =  event.getAction();
                final View view = (View) event.getLocalState();
                switch (dragEvent){
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(view.getId() == R.id.imgFeed) {
                           stopIdle();
                               characterImg.setImageResource(eatingImg);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if(view.getId() == R.id.imgFeed) {
                            CheckHealth();
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        if(view.getId() == R.id.imgFeed) {
                            HealthIncrease(10);
                            CheckHealth();
                        }
                        if(view.getId()==R.id.imgPlay){
                            if(!isPlayBallIn){
                                playBallArea.setVisibility(View.VISIBLE);
                                playBallArea.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        playballImg.setY(0);
                                        velocity=0;
                                        isPlayBallIn = true;
                                        screenHeight = playBallArea.getHeight();
                                        StartBallGame();
                                    }
                                });
                            }
                        }
                        break;
                }
                return true;
            }
        };
    }

    private void CheckCharacter(){
        if(isCat){
            charachterIdleImages = catIdleImages;
            characterDeadImages = catDeadImages;
            eatingImg = R.drawable.eating;
        }
        else{
            charachterIdleImages = dogIdleImages;
            characterDeadImages = dogDeadImages;
            eatingImg = R.drawable.dog_eat;
        }

    }

    private void CheckHealth(){
        if(healthProg.getProgress()<=10){
            DeadAnimation();
        }
        else{
            PlayIdle();
        }
    }

    private void HappinessIncrease(int happy)
    {
        happinessProg.setProgress(happinessProg.getProgress()+happy);
    }

    private void HealthIncrease(int health)
    {
        healthProg.setProgress(healthProg.getProgress()+health);
    }
    private void LoadComponents(){

        catIdleImages = new ArrayList<>();
        catIdleImages.add(R.drawable.idle_1);
        catIdleImages.add(R.drawable.idle_2);
        catIdleImages.add(R.drawable.idle_3);
        catIdleImages.add(R.drawable.idle_4);
        catIdleImages.add(R.drawable.idle_5);
        catIdleImages.add(R.drawable.idle_6);
        catIdleImages.add(R.drawable.idle_7);
        catIdleImages.add(R.drawable.idle_8);
        catIdleImages.add(R.drawable.idle_9);
        catIdleImages.add(R.drawable.idle_10);

        dogIdleImages = new ArrayList<>();
        dogIdleImages.add(R.drawable.dog_idle1);
        dogIdleImages.add(R.drawable.dog_idle2);
        dogIdleImages.add(R.drawable.dog_idle3);
        dogIdleImages.add(R.drawable.dog_idle4);
        dogIdleImages.add(R.drawable.dog_idle5);
        dogIdleImages.add(R.drawable.dog_idle6);
        dogIdleImages.add(R.drawable.dog_idle7);
        dogIdleImages.add(R.drawable.dog_idle8);
        dogIdleImages.add(R.drawable.dog_idle9);
        dogIdleImages.add(R.drawable.dog_idle10);

        catDeadImages = new ArrayList<>();
        catDeadImages.add(R.drawable.dead_1);
        catDeadImages.add(R.drawable.dead_2);
        catDeadImages.add(R.drawable.dead_3);
        catDeadImages.add(R.drawable.dead_4);
        catDeadImages.add(R.drawable.dead_5);
        catDeadImages.add(R.drawable.dead_6);
        catDeadImages.add(R.drawable.dead_7);
        catDeadImages.add(R.drawable.dead_8);
        catDeadImages.add(R.drawable.dead_9);
        catDeadImages.add(R.drawable.dead_10);

        dogDeadImages = new ArrayList<>();
        dogDeadImages.add(R.drawable.dog_dead1);
        dogDeadImages.add(R.drawable.dog_dead2);
        dogDeadImages.add(R.drawable.dog_dead3);
        dogDeadImages.add(R.drawable.dog_dead4);
        dogDeadImages.add(R.drawable.dog_dead5);
        dogDeadImages.add(R.drawable.dog_dead6);
        dogDeadImages.add(R.drawable.dog_dead7);
        dogDeadImages.add(R.drawable.dog_dead8);
        dogDeadImages.add(R.drawable.dog_dead9);
        dogDeadImages.add(R.drawable.dog_dead10);


        shitCollected = new ArrayList<>();

        characterImg = findViewById(R.id.imgCharacter);
        feedImg  = findViewById(R.id.imgFeed);
        cleanImg = findViewById(R.id.imgClean);
        shit1Img = findViewById(R.id.imgShit1);
        shit2Img = findViewById(R.id.imgShit2);
        shit3Img = findViewById(R.id.imgShit3);
        shit4Img = findViewById(R.id.imgShit4);
        shit5Img = findViewById(R.id.imgShit5);
        shadowImg = findViewById(R.id.imgShadow);
        healthProg =  findViewById(R.id.healthProgress);
        happinessProg =  findViewById(R.id.happinessProgress);
        playImg = findViewById(R.id.imgPlay);
        playballImg = findViewById(R.id.imgPlayBall);
        playBallArea= findViewById(R.id.fLayout_BallGame);

        cleanImg.setOnLongClickListener(itemClickListiner());
        feedImg.setOnLongClickListener(itemClickListiner());
        playImg.setOnLongClickListener(itemClickListiner());
        playBallArea.setOnTouchListener(BallAreaListener());

        characterImg.setOnDragListener(CharacterListener(characterImg));
        shit1Img.setOnDragListener(shitDragListiner(shit1Img));
        shit2Img.setOnDragListener(shitDragListiner(shit2Img));
        shit3Img.setOnDragListener(shitDragListiner(shit3Img));
        shit4Img.setOnDragListener(shitDragListiner(shit4Img));
        shit5Img.setOnDragListener(shitDragListiner(shit5Img));

        playBallArea.setVisibility(View.GONE);

    }

    private void StartBallGame(){
        ballGameLoop = new Runnable() {
            @Override
            public void run() {
                boolean isBallGraounded = false;
                float y = playballImg.getY();
                velocity += gravity;
                y += velocity;

                if(y+playballImg.getHeight()>screenHeight){
                    y=screenHeight-playballImg.getHeight();
                    velocity=0;
                    isBallGraounded = true;
                }

                if(y<0){
                    y=0;
                    velocity=0;
                }
                if(!isBallGraounded){
                    playballImg.setY(y);
                    handler.postDelayed(this,16);
                }
                else{
                    BallGameOver();
                }
            }
        };
        handler.post(ballGameLoop);
    }

    private void BallGameOver()
    {
        handler.removeCallbacks(ballGameLoop);
        playBallArea.setVisibility(View.GONE);
        isPlayBallIn = false;
    }


    private void PlayIdle()
    {
        isCharacterAlive=true;
        adjustShadow(30);
        if(!isIdle) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    characterImg.setImageResource(charachterIdleImages.get(idleImgIndex));
                    idleImgIndex = (idleImgIndex +1)%charachterIdleImages.size();
                    handler.postDelayed(this,60);
                }
            };
            handler.post(runnable);
            isIdle = true;
        }
    }
    private void adjustShadow(int shadowMargin){
        int dp = (int) (shadowMargin * getResources().getDisplayMetrics().density + 0.5f);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) shadowImg.getLayoutParams();
        params.setMargins(params.leftMargin,params.topMargin,params.rightMargin,dp);
        shadowImg.setLayoutParams(params);
    }

    private void DeadAnimation()
    {
        isCharacterAlive = false;
        adjustShadow(40);
        if(isIdle){
            stopIdle();
            isIdle = false;
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                characterImg.setImageResource(characterDeadImages.get(deadImgIndex));
                if(deadImgIndex+1<characterDeadImages.size())
                {
                    deadImgIndex++;
                    handler.postDelayed(this,60);
                }
                else{
                    handler.removeCallbacks(runnable);
                }
            }
        };
        handler.post(runnable);
    }

    private void stopIdle(){
        if(isIdle) {
            handler.removeCallbacks(runnable);
            isIdle = false;
        }
    }
    private void HideSystemUI() {
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