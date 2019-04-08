package com.example.courseclash;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeListView extends AppCompatActivity{

    public ArrayList<Recipe> recipeList = new ArrayList<>();
    ListView listView = null;
    FirebaseFirestore db = null;
    Recipe recipe = null;
    private RecipeViewAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        listView = findViewById(R.id.recipe_listview);

        db = FirebaseFirestore.getInstance();

        getRecipes();

        rAdapter = new RecipeViewAdapter(this,R.layout.recipe_list_item, recipeList);
        listView.setAdapter(rAdapter);

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
