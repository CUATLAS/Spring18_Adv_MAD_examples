package com.example.aileen.superheroes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by aileen on 4/2/18.
 */

public class Hero {
    private String universe;
    private ArrayList<String> superheroes = new ArrayList<>();

    //constructor
    private Hero(String univ, ArrayList<String> heroes){
        this.universe = univ;
        this.superheroes = new ArrayList<String>(heroes);
    }

    public static final Hero[] heroes = {
            new Hero("DC", new ArrayList<String>(Arrays.asList("Superman", "Batman", "Wonder Woman", "The Flash", "Green Arrow", "Catwoman"))),
            new Hero("Marvel", new ArrayList<String>(Arrays.asList("Iron Man", "Black Widow", "Captain America", "Jean Grey", "Thor", "Hulk")))
    };

    public String getUniverse(){
        return universe;
    }

    public ArrayList<String> getSuperheroes(){
        return superheroes;
    }

    public String toString(){
        return this.universe;
    }

}
