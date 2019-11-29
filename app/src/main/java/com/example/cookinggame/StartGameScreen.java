package com.example.cookinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class StartGameScreen extends AppCompatActivity implements View.OnClickListener {

    private final int MAXNUMBER_INGREDIENT = 8;
    private ImageView imageFood;
    private ImageView selector1, selector2;
    private LinearLayout readyScreen;
    private ConstraintLayout gameBackground;
    private ConstraintLayout recipeView1;
    private ConstraintLayout recipeView2;
    private List<ImageView> imageRecipe;
    private List<ImageView> imageItems;
    private TextView txtScore;
    private TextView txtFood;
    private TextView txtTime;
    private List<TextView> txtRecipeAmt;
    private ProgressBar progressBar;
    private Button btnCook;
    private Timer timer;
    private Timer showIngredientTimer;
    private Food food;
    private List<Ingredient> ingredients;
    public static int score = 0;
    private int secondForShowIngredient;
    private int secondForGame;
    private int selectedRecipe = 0;
    private boolean flagIngredeintTimer;
    private boolean flagGameTimer;
    private Animation animation;
    private Animation animation1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game_screen);
        MainActivity.BGMplayer.Stop();
        MainActivity.GameBGMplayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.gameplay_bgm));
        initData();
        iniUI();
        setBackground();
    }

    private void setBackground()
    {
        if(MainActivity.background== 1)
            gameBackground.setBackgroundResource(R.drawable.background);
        else if(MainActivity.background== 2)
            gameBackground.setBackgroundResource(R.drawable.night_woodenbridge);
        else if(MainActivity.background== 3)
            gameBackground.setBackgroundResource(R.drawable.stars);
    }
    private void StartShowIngredientTimer() {
        MainActivity.GameBGMplayer.Play(true);
        progressBar.setMax(secondForShowIngredient);
        showIngredientTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                IngredientTimerMethod();
            }
        }, 1000, 1000);
    }

    private void StartGameTimer()
    {
        hideRecipes();
        showIngredients();
        progressBar.setMax(secondForGame);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameTimerMethod();
            }
        },1000,1000);
    }

    private void IngredientTimerMethod()
    {
        if(flagIngredeintTimer)
        {
            this.runOnUiThread(ShowIngredientTimer_Tick);
        }
    }

    private void GameTimerMethod()
    {
        if(flagGameTimer)
            this.runOnUiThread(GameTimer_Tick);
    }
    private Runnable ShowIngredientTimer_Tick = new Runnable() {
        public void run() {

            txtTime.setText("Remaing " + (secondForShowIngredient) + " seconds");
            progressBar.setProgress(secondForShowIngredient);
            if(secondForShowIngredient == 0)
            {
                flagIngredeintTimer = false;
                showIngredientTimer.cancel();
                StartGameTimer();
            }
            else
                --secondForShowIngredient;

        }
    };

    private Runnable GameTimer_Tick = new Runnable() {
        public void run() {

            txtTime.setText("Remaing " + (secondForGame) + " seconds");
            progressBar.setProgress(secondForGame);
            if(secondForGame == 0)
            {
                flagGameTimer = false;
                timer.cancel();
                MainActivity.SoundEffectPlayer = new MediaPlayerHelper(MediaPlayer.create(getApplicationContext(),R.raw.game_over));
                MainActivity.SoundEffectPlayer.Play(false);
                CustomDialogClass myDialog = new CustomDialogClass(StartGameScreen.this);
                myDialog.show();
            }
            else
                --secondForGame;


        }
    };

    public void DatabaseInitialization()
    {
        DatabaseAdapter db = new DatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllFood();
        if(c.getCount() == 0) // if table is empty , initialize the data into the tblfood
        {
            db.insertFood("Honey Sauce Pork", "food_01");
            db.insertFood("Mushroom Soup", "food_02");
            db.insertFood("Half-cooked Egg", "food_03");
            db.insertFood("Pork cucumber", "food_04");
            db.insertFood("Mixed BBQ", "food_05");
        }

        c = db.getAllIngredient();
        if(c.getCount() == 0)
        {
            for(int i = 1; i <= 20; i++)
            {
                db.insertIngredient("ingredient"+i);
            }
        }

        c = db.getAllFoodIngredient();
        if(c.getCount() == 0)
        {
            //food 1, set 1
            db.insertFoodIngredient("1",1,1, 0);
            db.insertFoodIngredient("1",14,2, 0);
            db.insertFoodIngredient("1",6,1, 0);

            //food 1. set 2
            db.insertFoodIngredient("1",18,2, 1);
            db.insertFoodIngredient("1",2,2, 1);
            db.insertFoodIngredient("1",1,1, 1);
            db.insertFoodIngredient("1",14,2, 1);

            //food 2, set 1
            db.insertFoodIngredient("2",17,2, 0);
            db.insertFoodIngredient("2",8,1, 0);
            db.insertFoodIngredient("2",14,1, 0);
            db.insertFoodIngredient("2",19,1, 0);

            //food 2, set 2
            db.insertFoodIngredient("2",20,2, 1);
            db.insertFoodIngredient("2",10,2, 1);
            db.insertFoodIngredient("2",3,1, 1);
            db.insertFoodIngredient("2",8,2, 1);

            //food 3, set 1
            db.insertFoodIngredient("3",8,2, 0);
            db.insertFoodIngredient("3",1,1, 0);
            db.insertFoodIngredient("3",17,2, 0);
            db.insertFoodIngredient("3",15,2, 0);

            //food 3, set 2
            db.insertFoodIngredient("3",13,2, 1);
            db.insertFoodIngredient("3",8,3, 1);
            db.insertFoodIngredient("3",14,2, 1);
            db.insertFoodIngredient("3",9,1, 1);

            //food 4, set 1
            db.insertFoodIngredient("4",1,1, 0);
            db.insertFoodIngredient("4",14,2, 0);
            db.insertFoodIngredient("4",8,2, 0);

            //food 4, set 2
            db.insertFoodIngredient("4",3,2, 1);
            db.insertFoodIngredient("4",8,2, 1);
            db.insertFoodIngredient("4",14,1, 1);
            db.insertFoodIngredient("4",7,1, 1);

            //food 5, set 1
            db.insertFoodIngredient("5",13,1, 0);
            db.insertFoodIngredient("5",8,1, 0);
            db.insertFoodIngredient("5",14,1, 0);
            db.insertFoodIngredient("5",17,2, 0);

            db.insertFoodIngredient("5",2,2, 1);
            db.insertFoodIngredient("5",13,1, 1);
            db.insertFoodIngredient("5",18,2, 1);
            db.insertFoodIngredient("5",1,2, 1);
        }


    }
    public void FoodInitialization()
    {
        ArrayList<Food> foods = GlobalVar.foods;
        ArrayList<Ingredient> ingredients = GlobalVar.all_ingredients;
        final int min = 0;
        final int max = GlobalVar.foods.size()-1;
        final int random = new Random().nextInt((max - min) + 1) + min;
        food = new Food().clone(GlobalVar.foods.get(random));
        selectedRecipe = new Random().nextInt(2);
        System.out.println("aaa");
        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        final int min = 1;
        final int max = 5;
        final int random = new Random().nextInt((max - min) + 1) + min;
        Query query = databaseReference.orderByChild("foodID").equalTo(Integer.toString(random));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                     food = dataSnapshot1.getValue(Food.class);
                }
                iniUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("a");
            }
        });*/
        /*DatabaseAdapter db = new DatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllFood();
        if(c.getCount() != 0) // if table is not empty, get the food and assign to the local variable
        {
            //initialize food
            final int min = 1;
            final int max = 5;
            final int random = new Random().nextInt((max - min) + 1) + min;
            c = db.getFood(Integer.toString(random));
            food = new Food(c.getString(0),c.getString(1),c.getString(2));
            System.out.println(food.getName());

            selectedRecipe = new Random().nextInt(2);

            //initialize food's ingredients
            c = db.getFoodIngredient(food.getFoodID());
            for(int i = 0; i < c.getCount(); i++)
            {
                food.addIngredients(new Ingredient(c.getString(6),c.getInt(3),c.getInt(4)));
                c.moveToNext();
            }
            if(food.getAllIngredient().size() != MAXNUMBER_INGREDIENT)
            {
                int num_set1 = 0;
                int num_set2 = 0;
                for(int i = 0; i < food.getAllIngredient().size(); i++)
                {
                    if(food.getIngredient(i).getSet() == 0)
                        num_set1++;
                    else
                        num_set2++;
                }
                if(num_set1 < 4)
                {
                    for(int i = 0; i < 4-num_set1; ++i)
                        food.addIngredients(new Ingredient("empty",0,-1),i+num_set1);
                }
                if(num_set2 < 4)
                {
                    for(int i = 0; i < 4-num_set2; ++i)
                        food.addIngredients(new Ingredient("empty",0,-1),i+4+num_set2);
                }
            }
            System.out.println(food.getName());
        }
        db.close();*/
    }
    public void IngredientsInitialization()
    {
        ingredients = new ArrayList<>();
        for(int i = 0; i < food.getAllIngredient(selectedRecipe).size(); i++)
        {
            for(int j = 0; j < food.getAllIngredient(selectedRecipe).get(i).getQuantity(); j++)
            {
                ingredients.add(food.getAllIngredient(selectedRecipe).get(i));
            }
        }
        ArrayList<Ingredient> temp_ingredient1 = food.getAllIngredient(selectedRecipe);
        ArrayList<Ingredient> temp_ingredient2 = new ArrayList<>();
        for(int i = 0; i < GlobalVar.all_ingredients.size(); i++)
        {
            for(int j = 0; j < temp_ingredient1.size(); j++)
            {
                if(GlobalVar.all_ingredients.get(i).getImage().equals(temp_ingredient1.get(j).getImage()))
                {
                    break;
                }
                if(j == 3)
                    temp_ingredient2.add(GlobalVar.all_ingredients.get(i));
            }

        }
        int count = ingredients.size(); // get the size of current list for add ingredient to the left room of list.
        int min = 0;
        int max = temp_ingredient2.size()-1;
        for(int i = 0; i < (42-count);i++)
        {
            int random = new Random().nextInt((max - min) + 1) + min;
            ingredients.add(temp_ingredient2.get(random));
        }
        Collections.shuffle(ingredients);

        /*DatabaseAdapter db = new DatabaseAdapter(this);
        db.open();

        //init ingredients (food used ingredient)
        ingredients = new ArrayList<>();
        for(int i = 0; i < food.getAllIngredient(selectedRecipe).size(); i++)
        {
            for(int j = 0; j < food.getAllIngredient(selectedRecipe).get(i).getQuantity(); j++)
            {
                ingredients.add(food.getAllIngredient(selectedRecipe).get(i));
            }
        }

        //init ingredients (unused ingredient)
        ArrayList<String> temp = new ArrayList<>();
        for(Ingredient x: food.getAllIngredient(selectedRecipe))
            temp.add(x.getImage());

        ArrayList<Ingredient> ingredientFromDB = new ArrayList<>();
        Cursor c = db.getIngredients(temp);
        for(int i = 0; i < c.getCount(); i++)
        {
            ingredientFromDB.add(new Ingredient(c.getString(1),0,0));
            c.moveToNext();
        }

        int count = ingredients.size(); // get the size of current list for add ingredient to the left room of list.
        int min = 0;
        int max = ingredientFromDB.size()-1;
        for(int i = 0; i < (42-count);i++)
        {
            int random = new Random().nextInt((max - min) + 1) + min;
            ingredients.add(ingredientFromDB.get(random));
        }
        Collections.shuffle(ingredients);
        db.close();*/
    }

    public void initData()
    {
        flagGameTimer = true;
        flagIngredeintTimer = true;
        secondForShowIngredient = 10;
        secondForGame = 30;
        timer = new Timer();
        showIngredientTimer = new Timer();
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hide_animation);


        //DatabaseInitialization(); //
        FoodInitialization(); //
        IngredientsInitialization(); //
    }
    public void iniUI()
    {
        imageRecipe = new ArrayList<>();
        imageItems = new ArrayList<>();
        txtRecipeAmt = new ArrayList<>();

        imageFood = findViewById(R.id.imageFood);
        for(int i = 0; i < MAXNUMBER_INGREDIENT; i++)
        {
            imageRecipe.add((ImageView) findViewById(getResources().getIdentifier("imgRecipe"+ (i+1),"id",getPackageName())));
            txtRecipeAmt.add((TextView) findViewById(getResources().getIdentifier("txtRecipeAmt"+ (i+1),"id",getPackageName())));
        }
        for(int i = 0; i < 42; i++)
        {
            imageItems.add((ImageView) findViewById(getResources().getIdentifier("imgItem"+ (i+1),"id",getPackageName())));
        }

        readyScreen = findViewById(R.id.readyScreen);
        readyScreen.setOnClickListener(this);

        gameBackground = findViewById(R.id.game_background);
        recipeView1 = findViewById(R.id.recipeView);
        recipeView2 = findViewById(R.id.recipeView1);

        selector1 = findViewById(R.id.selector1);
        selector2 = findViewById(R.id.selector2);
        txtScore = findViewById(R.id.txtScore);
        txtFood = findViewById(R.id.txtFood);
        txtTime = findViewById(R.id.txtTime);
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setMax(secondForGame);
        btnCook = findViewById(R.id.btnCook);
        btnCook.setOnClickListener(this);

        txtScore.setText("Score: " + score);
        txtFood.setText(food.getName());
        imageFood.setImageResource(getImageId(food.getImage()));
        for(int i = 0; i < imageRecipe.size(); i++)
        {
            if(i < food.getAllIngredient().size())
            {
                imageRecipe.get(i).setImageResource(getImageId(food.getIngredient(i).getImage()));
                //txtRecipeAmt.get(i).setText("x"+food.getIngredient(i).getQuantity());
            }
            else
            {
                imageRecipe.get(i).setImageResource(R.drawable.empty);
            }
        }

        for(int i = 0; i < imageItems.size(); i++)
        {
            imageItems.get(i).setImageResource(R.drawable.empty);
        }

    }
    private void showIngredients()
    {
        for(int i = 0; i < imageItems.size(); i++)
        {
            imageItems.get(i).startAnimation(animation);
            imageItems.get(i).setImageResource(getImageId(ingredients.get(i).getImage()));
            imageItems.get(i).setOnClickListener(this);
        }
    }
    private void hideRecipes()
    {
        // for recipes
        for(int i = 0; i < imageRecipe.size(); i++)
        {
            imageRecipe.get(i).startAnimation(animation1);
            imageRecipe.get(i).setVisibility(View.INVISIBLE);
        }
        for(int i = 0; i < imageRecipe.size(); i++)
        {
            imageRecipe.get(i).startAnimation(animation);
            imageRecipe.get(i).setImageResource(R.drawable.unknow);
            imageRecipe.get(i).setVisibility(View.VISIBLE);
        }
        for(int j = 0; j < food.getAllIngredient(selectedRecipe).size(); j++)
        {
            int begin_index = food.getBeginIndexOfSet(selectedRecipe);
            txtRecipeAmt.get(j+begin_index).startAnimation(animation);
            if(food.getAllIngredient(selectedRecipe).get(j).getQuantity()!=0)
                txtRecipeAmt.get(j+begin_index).setText(food.getAllIngredient(selectedRecipe).get(j).getCollectedQty()+"/"+food.getAllIngredient(selectedRecipe).get(j).getQuantity());
        }
        if(selectedRecipe == 0)
        {
            selector1.setAnimation(animation);
            selector1.setImageResource(R.drawable.selector);
            recipeView2.setAlpha(0.5f);
        }

        else
        {
            selector2.setAnimation(animation);
            selector2.setImageResource(R.drawable.selector);
            recipeView1.setAlpha(0.5f);
        }
    }

    private void resetComponent()
    {
        for(int i = 0; i < MAXNUMBER_INGREDIENT; i++)
        {
            txtRecipeAmt.get(i).setText("");
        }
        for(int i = 0; i < imageItems.size(); i++)
        {
            imageItems.get(i).setOnClickListener(null);
        }
        recipeView1.setAlpha(1);
        recipeView2.setAlpha(1);
        selector1.setImageResource(0);
        selector2.setImageResource(0);
        timer.cancel();
        showIngredientTimer.cancel();
    }
    private int getImageId(String imageName){
        return getResources().getIdentifier("drawable/" + imageName, null, getPackageName());
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.readyScreen)
        {
            readyScreen.setOnClickListener(null);
            readyScreen.startAnimation(animation1);
            readyScreen.setVisibility(View.INVISIBLE);
            StartShowIngredientTimer();
        }
        else if(view.getId() == R.id.btnCook)
        {
            int c = food.getAllIngredient(selectedRecipe).size();
            int count = 0;
            for(Ingredient x : food.getAllIngredient(selectedRecipe))
            {
                if(x.checkCollectedQty())
                    count++;
            }
            if(count == c)
            {
                score += 100;
                txtScore.setText("Score: " + score);
                resetComponent();
                initData();
                iniUI();
                MainActivity.SoundEffectPlayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.cook));
                MainActivity.SoundEffectPlayer.Play(false);
                StartShowIngredientTimer();
            }
        }
        else
        {
            boolean checkTrue = false;
            for(int i = 0; i < 42; i++)
            {
                if(view.getId() == getResources().getIdentifier("imgItem"+ (i+1),"id",getPackageName()))
                {
                    for(int j = 0; j < food.getAllIngredient(selectedRecipe).size(); j++)
                    {
                        if(ingredients.get(i).getImage() == food.getAllIngredient(selectedRecipe).get(j).getImage())
                        {
                            int begin_index = food.getBeginIndexOfSet(selectedRecipe);
                            if(!food.getAllIngredient(selectedRecipe).get(j).checkCollectedQty())
                                food.getAllIngredient(selectedRecipe).get(j).AddCollectedQty();
                            imageItems.get(i).setImageResource(R.drawable.empty);
                            imageItems.get(i).setOnClickListener(null);
                            txtRecipeAmt.get(j+begin_index).setText(food.getAllIngredient(selectedRecipe).get(j).getCollectedQty()+"/"+food.getAllIngredient(selectedRecipe).get(j).getQuantity());
                            if(food.getAllIngredient(selectedRecipe).get(j).checkCollectedQty())
                                imageRecipe.get(j+begin_index).setImageResource(getImageId(food.getAllIngredient(selectedRecipe).get(j).getImage()));
                            checkTrue = true;
                            MainActivity.SoundEffectPlayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.correct));
                            MainActivity.SoundEffectPlayer.Play(false);
                        }
                    }
                }
            }
            if(!checkTrue)
            {
                MainActivity.SoundEffectPlayer = new MediaPlayerHelper(MediaPlayer.create(this,R.raw.wrong));
                MainActivity.SoundEffectPlayer.Play(false);
            }
        }
    }
}
