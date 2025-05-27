package com.example.tamagochi;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Home extends Fragment {

    private View view;
    private ImageView characterImg, cleanImg, shadowImg, playImg, playballImg, healthImg, happyImg, backBtn, nextBtn, shit1Img, shit2Img, shit3Img, shit4Img, shit5Img;
    private ImageView feedImg;
    private ProgressBar healthProg, happinessProg;
    private FrameLayout playBallArea;
    private TextView quantity, moneyValue;
    private static final int animationFrames = 60;

    private float velocity = 0, gravity = 0.7f, jumpStrangth = -16f;
    private int screenHeight;
    private boolean isPlayBallIn = false;
    private Runnable petIdle,petDied, ballGameLoop, healthDecey,happyDecay, cleanlinessDecay;
    private ArrayList<Integer> charachterIdleImages, characterDeadImages, catIdleImages, catDeadImages, dogIdleImages, dogDeadImages;
    private ArrayList<ImageView> shitArrayList;
    private ArrayList<ShopItemModel> foodInStorage;
    private int foodItemIndexSelected = 0, healthStatInc = 0, happyStatcInc = 0, idleImgIndex = 0, deadImgIndex = 0, eatingImg;

    private Random random;

    private MediaPlayer mediaPlayer, coinMediaPlayer;
    private PetInfoModel petInfoModel;
    private CredentialsModel credentialsModel;

    private Handler handler = new Handler();
    int currentMoney;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        LoadComponents();
        SetupPet();
        return view;
    }




    private View.OnLongClickListener itemClickListiner() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            }
        };

    }

    private void UpdateShitImages() {
        if (PetInfoModel.shitCollection.isEmpty()) {
            PetInfoModel.shitCollection.put(shit1Img.getId(), false);
            PetInfoModel.shitCollection.put(shit2Img.getId(), false);
            PetInfoModel.shitCollection.put(shit3Img.getId(), false);
            PetInfoModel.shitCollection.put(shit4Img.getId(), false);
            PetInfoModel.shitCollection.put(shit5Img.getId(), false);
            UpdateShitImages();
        } else {
            Set<Integer> shitIDs = PetInfoModel.shitCollection.keySet();
            for (Integer shitID : shitIDs) {
                if (PetInfoModel.shitCollection.get(shitID)) {
                    ShitVisibilty(shitArrayList, shitID, View.INVISIBLE);
                } else {
                    ShitVisibilty(shitArrayList, shitID, View.VISIBLE);
                }
            }
        }
    }

    private void ShitVisibilty(ArrayList<ImageView> shitArray, int shitID, int visibility) {
        for (ImageView shitImage : shitArray) {
            if (shitImage.getId() == shitID) {
                shitImage.setVisibility(visibility);
            }
        }

    }


    private View.OnDragListener shitDragListiner(ImageView shitImage) {
        return new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent = event.getAction();
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        final View view = (View) event.getLocalState();
                        if (view.getId() == R.id.imgClean) {
                            PetInfoModel.shitCollection.put(shitImage.getId(), true);
                            UpdateShitImages();
                        }
                        break;
                }
                return true;
            }
        };
    }

    private View.OnTouchListener BallAreaListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    velocity = jumpStrangth;
                    random = new Random();
                    if (PetInfoModel.isIsAlive()) {
                        int happyMultiplier = random.nextInt(6);
                        HappinessUpdate(1 * happyMultiplier);
                    }
                    int moneyAdd = random.nextInt(6);
                    int moneyChance = random.nextInt(4)+1;
                    if(moneyChance == 2)
                    {
                        AddMoney(moneyAdd);
                    }
                }
                return true;
            }
        };
    }

    private void AddMoney(int AmountToAdd)
    {
        DocumentReference  docPetRef = FirebaseFirestore.getInstance().collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI());
        docPetRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc =  task.getResult();
                    if(doc.exists())
                    {
                        int money = (doc.getLong(getString(R.string.MONEY)) != null)? doc.getLong(getString(R.string.MONEY)).intValue():0;
                        updateMoney(money + AmountToAdd);
                    }
                }
            }
        });
    }

    private void updateMoney(int newMoney)
    {
        DocumentReference ref = FirebaseFirestore.getInstance().collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI());
        ref.update(getString(R.string.MONEY),newMoney);
    }

    private void MoneyRealTimeListiner()
    {
        FirebaseFirestore.getInstance().collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value != null && value.exists())
                {
                    int money = (value.getLong(getString(R.string.MONEY)) != null)? value.getLong(getString(R.string.MONEY)).intValue():PetInfoModel.getMoney();
                    PetInfoModel.setMoney(money);
                    CheckMoney();

                    if((!coinMediaPlayer.isPlaying() && coinMediaPlayer != null) && isPlayBallIn)
                    {
                        coinMediaPlayer.start();
                        coinMediaPlayer.setVolume(0.3f,0.3f);
                    }
                }
            }
        });
    }

    private void CheckMoney()
    {
        moneyValue.setText(""+PetInfoModel.getMoney());
        Profile.updateCurrentMoney();
    }


    private View.OnDragListener CharacterListener(ImageView characterImg) {
        return new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent = event.getAction();
                final View view = (View) event.getLocalState();
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (view.getId() == R.id.imgFeed) {
                            StopIdle();
                            StopDead();
                            characterImg.setImageResource(eatingImg);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if (view.getId() == R.id.imgFeed) {
                            CheckHealth();
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        if (view.getId() == R.id.imgFeed) {
                            HealthUpdate(healthStatInc);
                            HappinessUpdate(happyStatcInc);
                            if (!PetInfoModel.foodStuck.isEmpty()) {
                                PetInfoModel.foodStuck.put(foodInStorage.get(foodItemIndexSelected), PetInfoModel.foodStuck.get(foodInStorage.get(foodItemIndexSelected)) - 1);
                                if (PetInfoModel.foodStuck.get(foodInStorage.get(foodItemIndexSelected)) <= 0) {
                                    PetInfoModel.foodStuck.remove(foodInStorage.get(foodItemIndexSelected));
                                    foodInStorage.remove(foodItemIndexSelected);
                                    foodItemIndexSelected=PetInfoModel.foodStuck.size()-1;
                                }
                                SetFoodStorage();
                            }
                            CheckHealth();
                        }
                        if (view.getId() == R.id.imgPlay) {
                            if (!isPlayBallIn) {
                                playBallArea.setVisibility(View.VISIBLE);
                                playBallArea.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        playballImg.setY(0);
                                        velocity = 0;
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

    public void SetFoodStorage() {
        if (PetInfoModel.foodStuck.size() <=0) {
            feedImg.setImageResource(R.drawable.feed);
            quantity.setVisibility(View.INVISIBLE);
            healthStatInc = 0;
            happyStatcInc = 0;
        } else {
            Set<ShopItemModel> foodKeys = PetInfoModel.foodStuck.keySet();
            foodInStorage.addAll(foodKeys);
            System.out.println(foodItemIndexSelected);
            feedImg.setImageResource(foodInStorage.get(foodItemIndexSelected).getItemImage());
            quantity.setVisibility(View.VISIBLE);
            int numQuantity = PetInfoModel.foodStuck.get(foodInStorage.get(foodItemIndexSelected));
            quantity.setText(""+numQuantity);
            healthStatInc = foodInStorage.get(foodItemIndexSelected).getHealthStat();
            happyStatcInc = foodInStorage.get(foodItemIndexSelected).getHappyStat();
        }
    }

    private void CheckCharacter() {
        if (PetInfoModel.getPetType().equals("Cat")) {
            charachterIdleImages = catIdleImages;
            characterDeadImages = catDeadImages;
            eatingImg = R.drawable.eating;
            healthImg.setImageResource(R.drawable.ic_health);
            happyImg.setImageResource(R.drawable.ic_happiness);
        } else {
            charachterIdleImages = dogIdleImages;
            characterDeadImages = dogDeadImages;
            eatingImg = R.drawable.dog_eat;
            healthImg.setImageResource(R.drawable.ic_doghealth);
            happyImg.setImageResource(R.drawable.ic_doghappiness);
        }

    }

    private void CheckHealth() {
        StopDead();
        StopIdle();
        if (PetInfoModel.getHealth() <= 0 ) {
            PetInfoModel.setIsAlive(false);
            DeadAnimation();
        } else {
            if(!PetInfoModel.isIsAlive())
            {
                Toast.makeText(getContext(), PetInfoModel.getPetName()+" Is Alive", Toast.LENGTH_SHORT).show();
                StartCleanDecay();
                StartHappyDecay();
                StartHealthDecay();
            }
            PetInfoModel.setIsAlive(true);
            PlayIdle();
        }
    }

    private void HappinessUpdate(int happy) {
        if (PetInfoModel.getHappy() <= 100) {
            int result ;
            if((PetInfoModel.getHappy() + happy)>0)
            {
                if((PetInfoModel.getHappy()+happy)>100)
                {
                    result = 100;
                }
                else
                {
                    result = PetInfoModel.getHappy() + happy;
                }
            }
            else
            {
                result = 0;
            }
            PetInfoModel.setHappy(result);
        }
        happinessProg.setProgress(PetInfoModel.getHappy());
    }

    private void HealthUpdate(int health) {
        if (PetInfoModel.getHealth() <= 100) {
            int result ;
            if((PetInfoModel.getHealth() + health)>0)
            {
                if((PetInfoModel.getHealth()+health)>100)
                {
                    result = 100;
                }
                else
                {
                    result = PetInfoModel.getHealth() + health;
                }
            }
            else
            {
                result = 0;
            }
            PetInfoModel.setHealth(result);
        }
        healthProg.setProgress(PetInfoModel.getHealth());
        System.out.println(PetInfoModel.getHealth());
        System.out.println(healthProg.getProgress());

        CheckHealth();
    }

    private void LoadComponents() {

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


        foodInStorage = new ArrayList<>();

        characterImg = view.findViewById(R.id.imgCharacter);
        feedImg = view.findViewById(R.id.imgFeed);
        cleanImg = view.findViewById(R.id.imgClean);
        shit1Img = view.findViewById(R.id.imgShit1);
        shit2Img = view.findViewById(R.id.imgShit2);
        shit3Img = view.findViewById(R.id.imgShit3);
        shit4Img = view.findViewById(R.id.imgShit4);
        shit5Img = view.findViewById(R.id.imgShit5);
        shadowImg = view.findViewById(R.id.imgShadow);
        healthProg = view.findViewById(R.id.healthProgress);
        happinessProg = view.findViewById(R.id.happinessProgress);
        quantity = view.findViewById(R.id.lblQuantity);
        playImg = view.findViewById(R.id.imgPlay);
        playballImg = view.findViewById(R.id.imgPlayBall);
        playBallArea = view.findViewById(R.id.fLayout_BallGame);
        healthImg = view.findViewById(R.id.healthImg);
        happyImg = view.findViewById(R.id.happyImg);
        backBtn = view.findViewById(R.id.btnBack);
        nextBtn = view.findViewById(R.id.btnNext);
        moneyValue = view.findViewById(R.id.LblMoney);

        shitArrayList = new ArrayList<>();
        shitArrayList.add(shit1Img);
        shitArrayList.add(shit2Img);
        shitArrayList.add(shit3Img);
        shitArrayList.add(shit4Img);
        shitArrayList.add(shit5Img);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.ballhit);
        coinMediaPlayer = MediaPlayer.create(getContext(),R.raw.coin);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PetInfoModel.foodStuck.isEmpty()) {
                    if (foodItemIndexSelected > 0) {
                        foodItemIndexSelected -= 1;
                    } else {
                        foodItemIndexSelected = PetInfoModel.foodStuck.size() - 1;
                    }
                }
                SetFoodStorage();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PetInfoModel.foodStuck.isEmpty()) {
                    if (foodItemIndexSelected < PetInfoModel.foodStuck.size() - 1) {
                        foodItemIndexSelected += 1;
                    } else {
                        foodItemIndexSelected = 0;
                    }
                }
                SetFoodStorage();
            }
        });

        cleanImg.setOnLongClickListener(itemClickListiner());
        feedImg.setOnLongClickListener(itemClickListiner());
        playImg.setOnLongClickListener(itemClickListiner());

        characterImg.setOnDragListener(CharacterListener(characterImg));
        shit1Img.setOnDragListener(shitDragListiner(shit1Img));
        shit2Img.setOnDragListener(shitDragListiner(shit2Img));
        shit3Img.setOnDragListener(shitDragListiner(shit3Img));
        shit4Img.setOnDragListener(shitDragListiner(shit4Img));
        shit5Img.setOnDragListener(shitDragListiner(shit5Img));

        playBallArea.setVisibility(View.GONE);

    }
    private void Setup()
    {
        DocumentReference docPetRef = FirebaseFirestore.getInstance().collection(getString(R.string.PETS)).document(CredentialsModel.getPetDI());
        docPetRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        String petType = doc.getString(getString(R.string.PET_GENDER));
                        PetInfoModel.setPetType(petType);
                        String petName = doc.getString(getString(R.string.PET_NAME));
                        PetInfoModel.setPetName(petName);
                        boolean status =  (doc.getBoolean(getString(R.string.IS_ALIVE)) !=null) ? doc.getBoolean(getString(R.string.IS_ALIVE)).booleanValue():false;
                        PetInfoModel.setIsAlive(status);
                        int money  =  (doc.getLong(getString(R.string.MONEY)) !=null)? doc.getLong(getString(R.string.MONEY)).intValue():0;
                        PetInfoModel.setMoney(money);
                        int health = (doc.get(getString(R.string.HEALTH)) != null)? doc.getLong(getString(R.string.HEALTH)).intValue():0;
                        PetInfoModel.setHealth(health);
                        int happiness = (doc.get(getString(R.string.HAPPINESS)) != null)? doc.getLong(getString(R.string.HAPPINESS)).intValue():0;
                        PetInfoModel.setHappy(happiness);


                        CheckCharacter();
                        CheckHealth();
                        UpdateShitImages();
                        SetFoodStorage();
                        healthProg.setProgress(PetInfoModel.getHealth());
                        happinessProg.setProgress(PetInfoModel.getHealth());
                        StartCleanDecay();
                        StartHappyDecay();
                        StartHealthDecay();
                        CheckMoney();
                        MoneyRealTimeListiner();
                    }
                }
            }
        });

    }
    private void SetupPet()
    {
        DocumentReference docUserRef = FirebaseFirestore.getInstance().collection(getString(R.string.USERS)).document(CredentialsModel.getCurrentUserUDI());
        docUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists())
                    {
                        CredentialsModel.setPetDI(doc.getString(getString(R.string.PET_OWNED)));
                        PetInfoModel.setParentName(doc.getString(getString(R.string.NAME_AS_PARENT)));
                        Setup();
                    }
                }
            }
        });
    }


    private boolean IsAllShitSpawed(Set<Integer> shitkeys)
    {
        for(Integer key: shitkeys)
        {
           if(PetInfoModel.shitCollection.get(key))
           {
               return false;
           }
        }
        return true;
    }
    private void ShitSpawner()
    {
        Set<Integer> shitkeys= PetInfoModel.shitCollection.keySet();
        if(!IsAllShitSpawed(shitkeys))
        {
            random = new Random();
            ArrayList<Integer> shitsNotSpawned = FilterAvailableShitToSpaw();
            int shitSelectedIndex = random.nextInt(shitsNotSpawned.size());
            int shitSelected = shitsNotSpawned.get(shitSelectedIndex);
            for(Integer keys: shitkeys)
            {
               if(keys == shitSelected)
               {
                   PetInfoModel.shitCollection.put(keys,false);
                   UpdateShitImages();
               }
            }
        }
    }
    private ArrayList<Integer> FilterAvailableShitToSpaw()
    {
        Set<Integer> shitkeys= PetInfoModel.shitCollection.keySet();
        ArrayList<Integer> filteredShits = new ArrayList<>();
        for(int key:shitkeys)
        {
            if(PetInfoModel.shitCollection.get(key))
            {
                filteredShits.add(key);
            }
        }
        return filteredShits;
    }

    private void StartCleanDecay() {
        cleanlinessDecay = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,17000);
                ShitSpawner();
            }
        };
        handler.post(cleanlinessDecay);
    }
    private void StartHappyDecay () {
        happyDecay = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,9000);
                if(PetInfoModel.getHappy()>0)
                {
                    random = new Random();
                    int happyDecay = random.nextInt(5)+1;
                    HappinessUpdate(-happyDecay);
                }
            }
        };
        handler.post(happyDecay);
    }
    private void StartHealthDecay() {
        healthDecey = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,18000);
                if(PetInfoModel.getHealth()>0)
                {
                    random = new Random();
                    int healthDecay = random.nextInt(5)+1;
                    HealthUpdate(-healthDecay);
                }
            }
        };
        handler.post(healthDecey);
    }

    private void StartBallGame(){
        ballGameLoop = new Runnable() {
            @Override
            public void run() {
                boolean isBallGraounded = false;
                float y = playballImg.getY();
                velocity += gravity;
                y += velocity;

                if(velocity>=0)
                {
                    playBallArea.setOnTouchListener(BallAreaListener());
                }
                else
                {
                    playBallArea.setOnTouchListener(null);
                }

                if(mediaPlayer.isPlaying() || mediaPlayer != null && velocity<-15)
                {
                    mediaPlayer.start();
                }

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
                    handler.postDelayed(this,12);
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

    private void adjustShadow(int shadowMargin){
        int dp = (int) (shadowMargin * getResources().getDisplayMetrics().density + 0.5f);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) shadowImg.getLayoutParams();
        params.setMargins(params.leftMargin,params.topMargin,params.rightMargin,dp);
        shadowImg.setLayoutParams(params);
    }

    private void PlayIdle()
    {
        adjustShadow(30);
        if(PetInfoModel.isIsAlive())
        {
            petIdle = new Runnable() {
                @Override
                public void run() {
                    characterImg.setImageResource(charachterIdleImages.get(idleImgIndex));
                    idleImgIndex = (idleImgIndex +1)%charachterIdleImages.size();
                    handler.postDelayed(this,animationFrames);
                }
            };
            handler.post(petIdle);
        }
    }

    private void DeadAnimation()
    {
        adjustShadow(40);
        if(!PetInfoModel.isIsAlive())
        {
            petDied = new Runnable() {
                @Override
                public void run() {
                    if(deadImgIndex+1<characterDeadImages.size())
                    {
                        deadImgIndex++;
                        characterImg.setImageResource(characterDeadImages.get(deadImgIndex));
                    }
                    handler.postDelayed(this,animationFrames);
                    if(deadImgIndex+1>=characterDeadImages.size())
                    {
                        deadImgIndex=0;
                        Toast.makeText(getContext(), PetInfoModel.getPetName()+" is Dead", Toast.LENGTH_SHORT).show();
                        HappinessUpdate(-PetInfoModel.getHappy());
                        StopDead();
                        StopHealthDecay();
                        StopHappyDecay();
                        StopCleanlinessDecay();
                    }
                }
            };
            handler.post(petDied);
        }
    }

    private void StopThreads()
    {
        StopDead();
        StopIdle();
        StopHappyDecay();
        StopHealthDecay();
        StopCleanlinessDecay();
    }

    private void StopDead()
    {
        handler.removeCallbacks(petDied);
    }
    private void StopIdle()
    {
        handler.removeCallbacks(petIdle);
    }

    private void StopHealthDecay()
    {
        handler.removeCallbacks(healthDecey);
    }
    private void StopHappyDecay()
    {
        handler.removeCallbacks(happyDecay);
    }
    private void StopCleanlinessDecay()
    {
        handler.removeCallbacks(cleanlinessDecay);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        StopThreads();
    }
}