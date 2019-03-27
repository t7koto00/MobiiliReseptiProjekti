package com.example.courseclash;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecipeView extends AppCompatActivity implements View.OnClickListener{

    public Button instructionsButton = null;
    public Button ingredientsButton = null;
    FloatingActionButton fabFavorite = null;
    FloatingActionButton fabRate = null;
    TextView textViewRecipe = null;
    TextView textViewTitle = null;
    TextView textViewTags = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ingredientsButton = findViewById(R.id.ingredientButton);
        instructionsButton = findViewById(R.id.instructionsButton);
        ingredientsButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);

        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fabFavorite.setOnClickListener(this);

        fabRate = (FloatingActionButton) findViewById(R.id.fabRate);
        fabRate.setOnClickListener(this);

        textViewRecipe = findViewById(R.id.textViewRecipe);
        textViewTags = findViewById(R.id.textViewTags);
        textViewTitle = findViewById(R.id.textViewTitle);

        getRecipe();

    }


    @Override
    public void onClick(View view) {
        if (view == ingredientsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
            instructionsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
            ViewCompat.setBackgroundTintList(ingredientsButton,getResources().getColorStateList(R.color.colorPrimary) );
            ViewCompat.setBackgroundTintList(instructionsButton,getResources().getColorStateList(R.color.colorWhite) );

            textViewRecipe.setText("1 pound ground lean (7% fat) beef\n 1 large egg\n 1/2 cup minced onion 1/4 cup fine dried bread crumbs 1 tablespoon Worcestershire 1 or 2 cloves garlic, peeled and minced About 1/2 teaspoon salt About 1/4 teaspoon pepper 4 hamburger buns (4 in. wide), split About 1/4 cup mayonnaise About 1/4 cup ketchup 4 iceberg lettuce leaves, rinsed and crisped 1 firm-ripe tomato, cored and thinly sliced 4 thin slices red onion\n");
        }
        else if (view == instructionsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
            instructionsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
            ViewCompat.setBackgroundTintList(ingredientsButton,getResources().getColorStateList(R.color.colorWhite) );
            ViewCompat.setBackgroundTintList(instructionsButton,getResources().getColorStateList(R.color.colorPrimary) );

            textViewRecipe.setText("Step 1\n" +
                    "In a bowl, mix ground beef, egg, onion, bread crumbs, Worcestershire, garlic, 1/2 teaspoon salt, and 1/4 teaspoon pepper until well blended. Divide mixture into four equal portions and shape each into a patty about 4 inches wide.\n\n Step 2\n" +
                    "Lay burgers on an oiled barbecue grill over a solid bed of hot coals or high heat on a gas grill (you can hold your hand at grill level only 2 to 3 seconds); close lid on gas grill. Cook burgers, turning once, until browned on both sides and no longer pink inside (cut to test), 7 to 8 minutes total. Remove from grill.\n" +
                    "\n" +
                    "Step 3\n" +
                    "Lay buns, cut side down, on grill and cook until lightly toasted, 30 seconds to 1 minute.\n" +
                    "\n" +
                    "Step 4\n" +
                    "Spread mayonnaise and ketchup on bun bottoms. Add lettuce, tomato, burger, onion, and salt and pepper to taste. Set bun tops in place.");
        }
        else if (view == fabFavorite){
            favorite();
        }
        else if (view == fabRate){
            rate();
        }
    }

    void favorite() {
        //lisää resepti favorite listalle
    }

    void rate() {
        //arvostele resepti
    }

    void getRecipe(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("recipes").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Success", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("No", "No such document");
                    }
                } else {
                    Log.d("Fail", "get failed with ", task.getException());
                }
            }
        });

    }
}
