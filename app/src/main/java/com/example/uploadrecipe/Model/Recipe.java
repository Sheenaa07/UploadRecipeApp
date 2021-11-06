package com.example.uploadrecipe.Model;

public class Recipe {
    String name;
    String ingredients;
    String instructions;
    String photoUrl;

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    String key;



    public Recipe(String name, String ingredients, String instructions, String photoUrl){
        this.name=name;
        this.ingredients=ingredients;
        this.instructions=instructions;
        this.photoUrl=photoUrl;
    }

    public Recipe(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getIngredients() {
        return ingredients;
    }
    public void setIngrdients(String ingredients) {
        this.ingredients = ingredients;

    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions=instructions;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl=photoUrl;

    }
}
