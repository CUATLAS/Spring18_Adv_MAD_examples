package com.example.aileen.superheroes;

import java.util.ArrayList;

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

}
