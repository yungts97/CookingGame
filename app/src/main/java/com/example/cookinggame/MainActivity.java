package com.example.cookinggame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnStart, btnSetting, btnInfo;
    public static MediaPlayerHelper BGMplayer;
    public static MediaPlayerHelper GameBGMplayer;
    public static MediaPlayerHelper SoundEffectPlayer;
    public static int background = 1;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniUI();
        initFireBase();
    }

    private void initFireBase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        String[] foodnames = getResources().getStringArray(R.array.foods);
        String[] foodimages = getResources().getStringArray(R.array.food_images);

        ArrayList<ArrayList<Ingredient>> ingredients = new ArrayList<>();
        //food1
        ArrayList<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient("ingredient1",1,0));
        ingredients1.add(new Ingredient("ingredient14",2,0));
        ingredients1.add(new Ingredient("ingredient6",1,0));
        ingredients1.add(new Ingredient("empty",0,0));

        ingredients1.add(new Ingredient("ingredient18",2,1));
        ingredients1.add(new Ingredient("ingredient2",2,1));
        ingredients1.add(new Ingredient("ingredient1",1,1));
        ingredients1.add(new Ingredient("ingredient14",2,1));

        //food2
        ArrayList<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient("ingredient17",2,0));
        ingredients2.add(new Ingredient("ingredient8",1,0));
        ingredients2.add(new Ingredient("ingredient14",1,0));
        ingredients2.add(new Ingredient("ingredient19",1,0));

        ingredients2.add(new Ingredient("ingredient20",2,1));
        ingredients2.add(new Ingredient("ingredient10",2,1));
        ingredients2.add(new Ingredient("ingredient3",1,1));
        ingredients2.add(new Ingredient("ingredient8",2,1));

        //food3
        ArrayList<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("ingredient8",2,0));
        ingredients3.add(new Ingredient("ingredient1",1,0));
        ingredients3.add(new Ingredient("ingredient17",2,0));
        ingredients3.add(new Ingredient("ingredient15",2,0));

        ingredients3.add(new Ingredient("ingredient13",2,1));
        ingredients3.add(new Ingredient("ingredient8",3,1));
        ingredients3.add(new Ingredient("ingredient14",2,1));
        ingredients3.add(new Ingredient("ingredient9",1,1));

        //food4
        ArrayList<Ingredient> ingredients4 = new ArrayList<>();
        ingredients4.add(new Ingredient("ingredient1",1,0));
        ingredients4.add(new Ingredient("ingredient14",2,0));
        ingredients4.add(new Ingredient("ingredient8",2,0));
        ingredients4.add(new Ingredient("empty",0,0));

        ingredients4.add(new Ingredient("ingredient3",2,1));
        ingredients4.add(new Ingredient("ingredient8",2,1));
        ingredients4.add(new Ingredient("ingredient14",1,1));
        ingredients4.add(new Ingredient("ingredient7",1,1));

        //food5
        ArrayList<Ingredient> ingredients5 = new ArrayList<>();
        ingredients5.add(new Ingredient("ingredient13",1,0));
        ingredients5.add(new Ingredient("ingredient8",1,0));
        ingredients5.add(new Ingredient("ingredient14",1,0));
        ingredients5.add(new Ingredient("ingredient17",2,0));

        ingredients5.add(new Ingredient("ingredient2",2,1));
        ingredients5.add(new Ingredient("ingredient13",1,1));
        ingredients5.add(new Ingredient("ingredient18",2,1));
        ingredients5.add(new Ingredient("ingredient1",2,1));

        ingredients.add(ingredients1);
        ingredients.add(ingredients2);
        ingredients.add(ingredients3);
        ingredients.add(ingredients4);
        ingredients.add(ingredients5);

        for(int i = 0; i < foodnames.length; i++)
        {
            String FoodID = databaseReference.push().getKey();
            Food food = new Food(FoodID,foodnames[i],foodimages[i]);
            food.setIngredients(ingredients.get(i));
            databaseReference.child(FoodID).setValue(food, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    System.out.println(databaseError);
                    System.out.println(databaseReference);
                    System.out.println("insert successfully");
                }
            });


        }

    }

    private void iniUI()
    {
        BGMplayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.bgm));
        BGMplayer.Play(true);
        SoundEffectPlayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.correct));

        btnStart = findViewById(R.id.btnStart);
        btnSetting = findViewById(R.id.btnSetting);
        btnInfo = findViewById(R.id.btnInfo);

        btnStart.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnStart)
        {

            Intent intent = new Intent(this,StartGameScreen.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnSetting)
        {
            Intent intent = new Intent(this,SettingScreen.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnInfo)
        {
            Intent intent = new Intent(this,HelpScreen.class);
            startActivity(intent);
        }
    }
}
