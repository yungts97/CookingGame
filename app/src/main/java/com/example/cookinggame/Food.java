package com.example.cookinggame;

import java.util.ArrayList;

public class Food {
    private String foodID;
    private String name;
    private String image;
    private ArrayList<Ingredient> ingredients;


    public Food(){
        foodID = "";
        name = "";
        image = "";
        ingredients = new ArrayList<>();
    }

    public Food(String ID, String name, String image, ArrayList<Ingredient> ingredients) {
        this.foodID = ID;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
    }

    public Food(String ID, String name, String image) {
        this.foodID = ID;
        this.name = name;
        this.image = image;
        this.ingredients = new ArrayList<>();
    }
    public Food clone(Food target)
    {
        this.foodID = target.foodID;
        this.name = target.name;
        this.image = target.image;
        this.ingredients = new ArrayList<>();
        for(Ingredient x : target.getAllIngredient())
        {
            ingredients.add(x.clone());
        }
        return this;
    }


    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Ingredient> getAllIngredient() {
        return ingredients;
    }

    public ArrayList<Ingredient> getAllIngredient(int set) {
        ArrayList<Ingredient> foodIngredients = new ArrayList<>();
        for(Ingredient x: ingredients)
        {
            if(x.getSet() == set)
            {
                foodIngredients.add(x);
            }
        }
        return foodIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredient getIngredient(int index)
    {
        return ingredients.get(index);
    }

    public void addIngredients(Ingredient ingredients) {
        this.ingredients.add(ingredients);
    }
    public void addIngredients(Ingredient ingredients, int index) {
        this.ingredients.add(index, ingredients);
    }
    public int getBeginIndexOfSet(int set)
    {
        int count = 0;
        for(Ingredient x:ingredients)
        {
            if(x.getSet() == set)
                return count;
            ++count;
        }
        return count;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean checkIsValidFood()
    {
        if(!foodID.equals("") && !name.equals("") && !image.equals(""))
        {
            if(ingredients.size() != 0)
            {
                boolean flag1 = false;
                boolean flag2 = false;
                for(int i = 0; i < 4; i++)
                {
                    if(!ingredients.get(i).getImage().equals("") && ingredients.get(i).getQuantity()!=0)
                    {
                        flag1 = true;
                    }
                }
                for(int i = 4; i < 8; i++)
                {
                    if(!ingredients.get(i).getImage().equals("") && ingredients.get(i).getQuantity()!=0)
                    {
                        flag2 = true;
                    }
                }
                if(flag1 && flag2)
                    return true;
            }
        }
        return false;
    }
}
