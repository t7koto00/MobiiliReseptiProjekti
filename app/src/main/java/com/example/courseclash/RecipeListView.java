package com.example.courseclash;


import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.support.v7.widget.SearchView;

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
    private RecipeViewAdapter rAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipe_view);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_recipe_view, null, false);
        drawer.addView(contentView, 0);

        listView = findViewById(R.id.recipe_listview);

        db = FirebaseFirestore.getInstance();

        getRecipes();

        rAdapter = new RecipeViewAdapter(this,R.layout.recipe_list_item, recipeList);
        listView.setAdapter(rAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),RecipeView.class);
                intent.putExtra("DATA", recipeList.get(position).getId());
                startActivity(intent);
            }
        });

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
