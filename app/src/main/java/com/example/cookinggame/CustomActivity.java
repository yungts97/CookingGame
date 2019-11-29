package com.example.cookinggame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class CustomActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageView selectedFood, selectedRecipeView1, selectedRecipeView2;
    private Button btnSave;
    private ConstraintLayout recipe1, recipe2;
    private EditText txtFoodName;
    private ArrayList<ImageView> imageFoods;
    private ArrayList<ImageView> imageIngredients;
    private ArrayList<Ingredient> recipeIngredient;
    private int selectedRecipe = 1;
    private Food newFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        init();
    }
    private void init()
    {
        newFood = new Food();
        int newFoodID = GlobalVar.foods.size() + 1;
        newFood.setFoodID(Integer.toString(newFoodID));

        recipe1 = findViewById(R.id.recipeView1);
        recipe1.setOnClickListener(this);
        recipe1.setOnLongClickListener(this);
        recipe2 = findViewById(R.id.recipeView2);
        recipe2.setOnClickListener(this);
        recipe2.setOnLongClickListener(this);

        selectedFood = findViewById(R.id.image_Food);
        imageFoods = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            String imageID = "food" + (i+1);
            int resID = getResources().getIdentifier(imageID, "id", getPackageName());
            imageFoods.add((ImageView)findViewById(resID));
            imageFoods.get(i).setOnClickListener(this);
        }

        imageIngredients = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            String imageID = "imgItem" + (i+1);
            int resID = getResources().getIdentifier(imageID, "id", getPackageName());
            imageIngredients.add((ImageView)findViewById(resID));
            imageIngredients.get(i).setOnClickListener(this);
        }
        txtFoodName = findViewById(R.id.textFoodName);
        txtFoodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newFood.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        selectedRecipeView1 = findViewById(R.id.selector1);
        selectedRecipeView2 = findViewById(R.id.selector2);

        recipeIngredient = new ArrayList<>();
        for(int i = 0; i <4; i++)
        {
            recipeIngredient.add(new Ingredient("empty",0,0));
        }
        for(int i = 0; i <4; i++)
        {
            recipeIngredient.add(new Ingredient("empty",0,1));
        }

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave)
        {
            newFood.setIngredients(recipeIngredient);
            if(newFood.checkIsValidFood())
            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
                String FoodID = databaseReference.push().getKey();
                databaseReference.child(FoodID).setValue(newFood, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            System.out.println(databaseError);
                            System.out.println(databaseReference);
                            System.out.println("insert successfully");
                        }
                    });
                finish();
            }
            else
            {
                Toast.makeText(this,"Please fill all the essential input!!!",Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == R.id.recipeView1)
        {
            selectedRecipeView1.setVisibility(View.VISIBLE);
            selectedRecipeView2.setVisibility(View.INVISIBLE);
            selectedRecipe = 1;
        }
        else if(v.getId() == R.id.recipeView2)
        {
            selectedRecipeView1.setVisibility(View.INVISIBLE);
            selectedRecipeView2.setVisibility(View.VISIBLE);
            selectedRecipe = 2;
        }
        else
        {
            for(int i = 0; i < 10; i++) // for food
            {
                String imageID = "food" + (i+1);
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                if(v.getId() == resID)
                {
                    String selectedImage = "food_"+ String.format("%02d",i+1);
                    int selectedID = getResources().getIdentifier(selectedImage, "drawable", getPackageName());
                    selectedFood.setImageResource(selectedID);
                    newFood.setImage(selectedImage);
                }
            }

            for(int i = 0; i < 20; i++) // for ingredients
            {
                String imageID = "imgItem" + (i+1);
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                if(v.getId() == resID)
                {
                    if(selectedRecipe == 1)
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            String ingredientID = "imgRecipe" + (j+1);
                            int res_ID = getResources().getIdentifier(ingredientID, "id", getPackageName());
                            ImageView recipe = findViewById(res_ID);
                            if(recipeIngredient.get(j).getImage().equals("empty"))
                            {
                                String selectedImage = "ingredient" + (i + 1);
                                int selectedID = getResources().getIdentifier(selectedImage, "drawable", getPackageName());
                                if(!checkExistedIngredient(selectedImage,recipeIngredient.get(j).getSet()))
                                {
                                    recipe.setImageResource(selectedID);
                                    recipeIngredient.get(j).setImage(selectedImage);
                                    final int min = 1;
                                    final int max = 3;
                                    final int random = new Random().nextInt((max - min) + 1) + min;
                                    recipeIngredient.get(j).setQuantity(random);
                                }
                                break;
                            }
                        }
                    }
                    else
                    {
                        for(int j = 4; j < 8; j++)
                        {
                            String ingredientID = "imgRecipe" + (j+1);
                            int res_ID = getResources().getIdentifier(ingredientID, "id", getPackageName());
                            ImageView recipe = findViewById(res_ID);
                            if(recipeIngredient.get(j).getImage().equals("empty"))
                            {
                                String selectedImage = "ingredient" + (i + 1);
                                int selectedID = getResources().getIdentifier(selectedImage, "drawable", getPackageName());
                                if(!checkExistedIngredient(selectedImage,recipeIngredient.get(j).getSet()))
                                {
                                    recipe.setImageResource(selectedID);
                                    recipeIngredient.get(j).setImage(selectedImage);
                                    final int min = 1;
                                    final int max = 3;
                                    final int random = new Random().nextInt((max - min) + 1) + min;
                                    recipeIngredient.get(j).setQuantity(random);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    private boolean checkExistedIngredient(String ingredient, int set)
    {
        if(set == 0)
        {
            for(int i = 0; i < 4; i++)
            {
                if(recipeIngredient.get(i).getImage().equals(ingredient))
                {
                    return true;
                }
            }
        }
        else
        {
            for(int i = 4; i < 8; i++)
            {
                if(recipeIngredient.get(i).getImage().equals(ingredient))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.recipeView1)
        {
            for(int i = 0; i < 4; i++)
            {
                String imageID = "imgRecipe" + (i+1);
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                ImageView recipeImage = findViewById(resID);
                recipeImage.setImageResource(R.drawable.empty);
                recipeIngredient.get(i).setImage("empty");
                recipeIngredient.get(i).setQuantity(0);
            }
        }
        else if(v.getId() == R.id.recipeView2)
        {
            for(int i = 4; i < 8; i++)
            {
                String imageID = "imgRecipe" + (i+1);
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                ImageView recipeImage = findViewById(resID);
                recipeImage.setImageResource(R.drawable.empty);
                recipeIngredient.get(i).setImage("empty");
                recipeIngredient.get(i).setQuantity(0);
            }
        }
        return false;
    }
}
