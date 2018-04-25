package com.example.aileen.recipes;

/**
 * Created by aileen on 4/24/17.
 */

public class RecipeItem {
    private String id;
    private String name;
    private String url;

    public RecipeItem(){
        // Default constructor required for calls to DataSnapshot.getValue(RecipeItem.class)
    }

    public RecipeItem(String newid, String newName, String newURL){
        id = newid;
        name = newName;
        url = newURL;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String geturl(){
        return url;
    }

    //the string representation of a recipe name
    public String toString(){
        return this.name;
    }
}
