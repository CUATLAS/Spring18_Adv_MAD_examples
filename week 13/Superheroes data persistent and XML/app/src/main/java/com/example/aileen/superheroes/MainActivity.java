package com.example.aileen.superheroes;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity implements UniverseListFragment.UniverseListListener, HeroDetailFragment.ButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load data
        if(Hero.heroes.isEmpty()) {
            loadHeroes(this);
        }
    }

    public void loadHeroes(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences("Superheroes", Context.MODE_PRIVATE);
        //create an editor to read from the shared preferences file
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //load all data into a Map
        Map<String, ?> alldata = sharedPrefs.getAll();
        //if there's data in the map
        if (alldata.size() > 0) {
            for (Map.Entry<String, ?> entry : alldata.entrySet()) {
                //Log.d("map values", entry.getKey() + ": " + entry.getValue());
                //key is the universe name
                String newuniverse = (String) entry.getKey();
                ArrayList<String> herolist = new ArrayList<String>();
                //add the heroes String set to the ArrayList
                herolist.addAll(sharedPrefs.getStringSet(newuniverse, null));
                //create new Hero object
                Hero new_hero = new Hero(newuniverse, herolist);
                //add hero
                Hero.heroes.add(new_hero);
            }
        } else{ //no data, load XML file
            try {
                loadXML(this);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private void loadXML(Activity activity) throws XmlPullParserException, IOException {
        Log.d("xml", "inloadxml");
        String new_universe = new String();
        ArrayList<String> new_heroes=new ArrayList<String>();
        //string for debugging puposes only
        StringBuffer stringBuffer = new StringBuffer();
        //get xml file
        XmlResourceParser xpp = getResources().getXml(R.xml.superheroes);
        //advances the parser to the next event
        xpp.next();
        //gets the event type/state of the parser
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    // start of document
                    break;
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("universe")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                    }
                    if (xpp.getName().equals("name")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                        eventType = xpp.next();
                        new_universe = xpp.getText(); //gets the name of the universe
                        //stringBuffer.append("\nuniverse_name: " + new_universe);
                    } else if (xpp.getName().equals("hero")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                        eventType = xpp.next();
                        //add to arraylist
                        new_heroes.add(xpp.getText()); //gets the name of the hero
                        //stringBuffer.append("\nnew_heroes: " + new_heroes);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    //if end of universe add the new hero
                    if (xpp.getName().equals("universe")) {
                        //at the end of the universe
                        //create new Hero object
                        Hero new_hero = new Hero(new_universe, new_heroes);
                        //add hero
                        Hero.heroes.add(new_hero);
                        //clear universe name and list of heroes
                        new_universe = "";
                        new_heroes.clear();
                        //stringBuffer.append("\nnew hero: " + new_hero);
                    }
                    break;

                case XmlPullParser.TEXT:
                    break;
            }
            //advances the parser to the next event
            eventType = xpp.next();
        }
        System.out.println(stringBuffer.toString());
    }

    @Override public void itemClicked(long id){
        //get a reference to the frame layout that contains HeroDetailFragment
        View fragmentContainer = findViewById(R.id.fragment_container);
        //large layout device
        if (fragmentContainer != null) {
            //create new fragment instance
            HeroDetailFragment frag = new HeroDetailFragment();
            //set the id of the universe selected
            frag.setUniverse(id);

            //create new fragment transaction
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //replace the fragment in the fragment container
            ft.replace(R.id.fragment_container, frag);
            //add fragment to the back stack
            ft.addToBackStack(null);
            //set the transition animation-optional
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //commit the transaction
            ft.commit();
        } else { //app is running on a device with a smaller screen
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", (int) id);
            startActivity(intent);
        }
    }

    @Override public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override public void addheroclicked(View view){
        HeroDetailFragment fragment = (HeroDetailFragment)getFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.addhero();
    }

}
