package com.example.cookinggame;

public class Ingredient {
    private int ingredientID;
    private String image;
    private int quantity;
    private int collectedQty;
    private int set;

    public Ingredient(){}

    public Ingredient(int ingredientID, String image, int quantity, int set)
    {
        this.ingredientID = ingredientID;
        this.image = image;
        this.quantity = quantity;
        this.collectedQty = 0;
        this.set = set;
    }

    public Ingredient(String image, int quantity, int set) {
        this.ingredientID = 0;
        this.image = image;
        this.quantity = quantity;
        this.collectedQty = 0;
        this.set = set;
    }

    public Ingredient clone()
    {
        return new Ingredient(this.ingredientID, this.image,this.quantity,this.set);
    }
    public boolean checkCollectedQty() {
        return quantity == collectedQty;
    }

    public int getCollectedQty() {
        return collectedQty;
    }

    public void AddCollectedQty() {
        ++collectedQty;
    }

    public long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getImage() {
        return image;
    }

    public String getShadowImage()
    {
        String num = image.replaceAll("[^0-9]","");
        String f = image.replace(num,"");
        return (f + "shadow"+ num);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
