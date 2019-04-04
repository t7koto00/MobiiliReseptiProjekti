package com.example.courseclash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddRecipe extends AppCompatActivity implements View.OnClickListener {

    public EditText editTextTitle = null;
    public EditText editTextTime = null;
    public EditText editTextTags = null;
    public EditText editTextInstructions = null;
    public EditText editTextIngredients = null;
    public Button galleryButton = null;
    public Button cameraButton = null;
    public Button addButton = null;
    Recipe recipe = new Recipe();
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextIngredients = findViewById(R.id.editTextIngredients);
        editTextInstructions= findViewById(R.id.editTextInstructions);
        editTextTags = findViewById(R.id.editTextTags);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTitle = findViewById(R.id.editTextTitle);
        galleryButton = findViewById(R.id.galleryButton);
        cameraButton = findViewById(R.id.cameraButton);
        addButton = findViewById(R.id.addButton);
        galleryButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view == cameraButton) {
        //kamera kuvan ottoa varten jos mahdollista
        }
        else if (view == galleryButton){
        // kuvan valinta galleriasta
        }
        else if (view == addButton){

            addRecipeToDb();
        }
        }

        void addRecipeToDb()
    {
        recipe.setTags(editTextTags.getText().toString());
        recipe.setTitle(editTextTitle.getText().toString());
        recipe.setTime(editTextTime.getText().toString());
        recipe.setInstructions(editTextInstructions.getText().toString());
        recipe.setIngredients(editTextIngredients.getText().toString());
        //kuva!!
        db.collection("recipes").add(recipe).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
            recipe.setId(documentReference.getId());
            String id = recipe.getId();
            db.collection("recipes").document(recipe.getId()).set(recipe);

            Intent intent = new Intent(getApplicationContext(), RecipeView.class);
            intent.putExtra("DATA", id);
            startActivity(intent);
            finish();
        }
    });

    }

}
