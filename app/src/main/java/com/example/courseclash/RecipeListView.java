package com.example.courseclash;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecipeListView extends BaseActivity {

    public ArrayList<Recipe> recipeList = new ArrayList<>();
    ListView listView = null;
    FirebaseFirestore db = null;
    Recipe recipe = null;
    public RecipeViewAdapter rAdapter;
    public Button create_recipe_button;
    public Button ingredients_search_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipe_view);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_recipe_view, null, false);
        drawer.addView(contentView, 0);

        listView = findViewById(R.id.recipe_listview);
        create_recipe_button = findViewById(R.id.create_recipe_button);
        ingredients_search_button = findViewById(R.id.ingredients_search_button);


        db = FirebaseFirestore.getInstance();

        getRecipes();
        final Intent intent = new Intent(this, AddRecipe.class);

        create_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                startActivity(intent);

            }
        });

        ingredients_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeListView.this, "Work in progress", Toast.LENGTH_SHORT).show();
            }
        });

        rAdapter = new RecipeViewAdapter(this, R.layout.recipe_list_item, recipeList);
        listView.setAdapter(rAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), RecipeView.class);
                intent.putExtra("DATA", recipeList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void getRecipes() {
        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Tag", document.getId() + " => " + document.getData());
                                recipe = document.toObject(Recipe.class);
                                recipeList.add(recipe);
                                rAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("errortag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
