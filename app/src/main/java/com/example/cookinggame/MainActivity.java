package com.example.cookinggame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnStart, btnSetting, btnInfo, btnMenu, btnCustom;
    private TextView txtCustom, txtSetting;
    public static MediaPlayerHelper BGMplayer;
    public static MediaPlayerHelper GameBGMplayer;
    public static MediaPlayerHelper SoundEffectPlayer;
    public static int background = 1;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFireBase();
    }

    private void retrieveFoodsFromFirebase()
    {
        GlobalVar.foods.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    GlobalVar.foods.add(dataSnapshot1.getValue(Food.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("a");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Foods")) {
                    retrieveFoodsFromFirebase();
                }
                if(snapshot.hasChild("Score"))
                {
                    retrieveScoreFromFirebase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveIngredientsFromFirebase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingredients");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    GlobalVar.all_ingredients.add(dataSnapshot1.getValue(Ingredient.class));
                }
                iniUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("a");
            }
        });
    }
    private void retrieveScoreFromFirebase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Score");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GlobalVar.Score = dataSnapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("a");
            }
        });
    }
    private void initFireBase()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("Foods")) {
                    InitFoodFireBase();
                }
                else
                {
                    retrieveFoodsFromFirebase();
                }
                if(!snapshot.hasChild("Ingredients"))
                {
                    InitIngredientFireBase();
                }
                else
                {
                    retrieveIngredientsFromFirebase();
                }
                if(!snapshot.hasChild("Score"))
                {
                    InitScore();
                }
                else
                {
                    retrieveScoreFromFirebase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void InitScore()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Score");
        databaseReference.setValue(GlobalVar.Score, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    System.out.println(databaseError);
                    System.out.println(databaseReference);
                    System.out.println("insert successfully");
                }
            });

    }

    private void InitFoodFireBase()
    {
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        for(int i = 0; i < foodnames.length; i++) // set foods into firebase
        {
            String FoodID = databaseReference.push().getKey();
            Food food = new Food(Integer.toString(i+1),foodnames[i],foodimages[i]);
            GlobalVar.foods.add(food);  //initialize all foods (first time run only)
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

    private void InitIngredientFireBase()
    {
        ArrayList<Ingredient> all_Ingredient = new ArrayList<>();
        for(int i = 1; i <=20; i++)
        {
            all_Ingredient.add(new Ingredient("ingredient"+i,0,0));
        }

        GlobalVar.all_ingredients = all_Ingredient;  //initialize all ingredients (first time run only)

        databaseReference = FirebaseDatabase.getInstance().getReference("Ingredients");
        for(int i = 0; i < 20; i++) // set ingredients into firebase
        {
            String ingredientID = databaseReference.push().getKey();
            databaseReference.child(ingredientID).setValue(all_Ingredient.get(i), new DatabaseReference.CompletionListener() {
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
        btnMenu = findViewById(R.id.btnMenu);
        btnSetting = findViewById(R.id.btnSetting);
        btnCustom = findViewById(R.id.btnCustom);
        btnInfo = findViewById(R.id.btnInfo);

        txtCustom = findViewById(R.id.txtCustom);
        txtSetting = findViewById(R.id.txtSetting);

        txtCustom.setOnClickListener(this);
        txtSetting.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnStart)
        {

            Intent intent = new Intent(this,StartGameScreen.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.txtCustom)
        {
            Intent intent = new Intent(this,CustomActivity.class);
            startActivity(intent);
            btnSetting.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            txtSetting.setVisibility(View.INVISIBLE);
            txtCustom.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.txtSetting)
        {
            Intent intent = new Intent(this,SettingScreen.class);
            startActivity(intent);
            btnSetting.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            txtSetting.setVisibility(View.INVISIBLE);
            txtCustom.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.btnMenu)
        {
            if(btnSetting.getVisibility() == View.VISIBLE)
            {
                btnSetting.setVisibility(View.INVISIBLE);
                btnCustom.setVisibility(View.INVISIBLE);
                txtSetting.setVisibility(View.INVISIBLE);
                txtCustom.setVisibility(View.INVISIBLE);

            }
            else
            {
                btnSetting.setVisibility(View.VISIBLE);
                btnCustom.setVisibility(View.VISIBLE);
                txtSetting.setVisibility(View.VISIBLE);
                txtCustom.setVisibility(View.VISIBLE);
            }

        }
        else if(view.getId() == R.id.btnSetting)
        {
            Intent intent = new Intent(this,SettingScreen.class);
            startActivity(intent);
            btnSetting.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            txtSetting.setVisibility(View.INVISIBLE);
            txtCustom.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.btnCustom)
        {
            Intent intent = new Intent(this,CustomActivity.class);
            startActivity(intent);
            btnSetting.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            txtSetting.setVisibility(View.INVISIBLE);
            txtCustom.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.btnInfo)
        {
            Intent intent = new Intent(this,HelpScreen.class);
            startActivity(intent);
            btnSetting.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            txtSetting.setVisibility(View.INVISIBLE);
            txtCustom.setVisibility(View.INVISIBLE);
        }
    }
}
