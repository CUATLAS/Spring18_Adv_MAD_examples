package com.example.aileen.superheroes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aileen on 4/2/18.
 */

public class Hero {
    private String universe;
    private ArrayList<String> superheroes = new ArrayList<>();

    //constructor
    public Hero(String univ, ArrayList<String> heroes){
        this.universe = univ;
        this.superheroes = new ArrayList<String>(heroes);
    }

    public static ArrayList<Hero> heroes = new ArrayList<Hero>();

    public String getUniverse(){
        return universe;
    }

    public ArrayList<String> getSuperheroes(){
        return superheroes;
    }

    public String toString(){
        return this.universe;
    }

    public void storeHeroes(Context context, long universeId){
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences("Superheroes", Context.MODE_PRIVATE);
        //create an editor to write to the shared preferences file
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //create a set
        Set<String> set = new HashSet<String>();
        //add heroes to the set
        set.addAll(heroes.get((int) universeId).getSuperheroes());
        //pass the key/value pair to the shared preference file
        editor.putStringSet(heroes.get((int) universeId).getUniverse(), set);
        //save changes
        editor.commit();
    }
}
