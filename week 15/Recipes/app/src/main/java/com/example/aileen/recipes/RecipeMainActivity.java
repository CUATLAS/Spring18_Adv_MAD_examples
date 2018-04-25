package com.example.aileen.recipes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeMainActivity extends AppCompatActivity {

    // Firebase database instance
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Firebase database reference
    DatabaseReference ref = database.getReference();
    //Firebase database recipe node reference
    DatabaseReference reciperef = database.getReference("recipes");

    //array list of recipes
    List recipes = new ArrayList<>();
    //array adapter
    ArrayAdapter<RecipeItem> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(RecipeMainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText nameEditText = new EditText(RecipeMainActivity.this);
                nameEditText.setHint("Recipe name");
                layout.addView(nameEditText);
                final EditText urlEditText = new EditText(RecipeMainActivity.this);
                urlEditText.setHint("URL");
                layout.addView(urlEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecipeMainActivity.this);
                dialog.setTitle("Add Recipe");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get entered data
                        String recipeName = nameEditText.getText().toString();
                        String recipeURL = urlEditText.getText().toString();
                        if (recipeName.trim().length() > 0) {
                            //get new id from firebase
                            String key = reciperef.push().getKey();
                            //create new recipe item
                            RecipeItem newRecipe = new RecipeItem(key, recipeName, recipeURL);
                            //add to Firebase
                            reciperef.child(key).child("name").setValue(newRecipe.getName());
                            reciperef.child(key).child("url").setValue(newRecipe.geturl());
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });

        ListView recipeList = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<RecipeItem>(this, android.R.layout.simple_list_item_1, recipes);
        recipeList.setAdapter(listAdapter);

        // Read from the database
        ValueEventListener firebaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //empty the arraylist
                recipes.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // gets the item id
                    String newId = snapshot.getKey();
                    //get recipe from the snapshot
                    RecipeItem recipeItem = snapshot.getValue(RecipeItem.class);
                    //create new RecipeItem object
                    RecipeItem newRecipe = new RecipeItem(newId, recipeItem.getName(), recipeItem.geturl());
                    //add new recipe to our array
                    recipes.add(newRecipe);
                    //Log.d("data", "Value is: " + newRecipe.getId() + newRecipe.getName() + newRecipe.geturl());
                }
                //update adapter
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("oncreate", "Failed to read value.", error.toException());
            }
        };

        //add listener to the database recipe node reference
        reciperef.addValueEventListener(firebaseListener);
        registerForContextMenu(recipeList);

        //create listener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position, long id){
                //get tapped recipe
                RecipeItem recipeTapped = (RecipeItem) recipes.get(position);
                //get the recipe url
                String recipeURL = recipeTapped.geturl();
                //create new intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //add url to intent
                intent.setData(Uri.parse(recipeURL));
                //start intent
                startActivity(intent);
            }
        };
        recipeList.setOnItemClickListener(itemClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        //cast ContextMenu.ContextMenuInfo to AdapterView.AdapterContextMenuInfo since we're using an adapter
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //get recipe name that was pressed
        String recipename = ((TextView) adapterContextMenuInfo.targetView).getText().toString();
        //set the menu title
        menu.setHeaderTitle("Delete " + recipename);
        //add the choices to the menu
        menu.add(1, 1, 1, "Yes");
        menu.add(2, 2, 2, "No");
    }

    @Override public boolean onContextItemSelected(MenuItem item){
        //get the id of the item
        int itemId = item.getItemId();
        if (itemId == 1) { //if yes menu item was pressed
            //get the position of the menu item
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //get recipe that was pressed
            RecipeItem selectedRecipe = (RecipeItem) recipes.get(info.position);
            //get recipe id
            String recipeid = selectedRecipe.getId();
            //delete from Firebase
            reciperef.child(recipeid).removeValue();
        }
        return true;
    }


}
