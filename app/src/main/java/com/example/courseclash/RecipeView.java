package com.example.courseclash;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class RecipeView extends AppCompatActivity implements View.OnClickListener{

    public Button instructionsButton = null;
    public Button ingredientsButton = null;
    public Button commentButton = null;
    public FloatingActionButton fabFavorite = null;
    public FloatingActionButton fabRate = null;
    public TextView textViewRecipe = null;
    public TextView textViewTitle = null;
    public TextView textViewTags = null;
    public TextView textViewTime = null;
    public TextView textViewAuthor = null;
    public EditText commentText = null;
    public FirebaseFirestore db = null;
    public Recipe recipe = null;
    ArrayList<String> comments = new ArrayList<>();
    ListView commentList = null;
    commentArrayAdapter arrayAdapter = null;
    public ImageView imageview = null;
    int stars = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ingredientsButton = findViewById(R.id.ingredientButton);
        instructionsButton = findViewById(R.id.instructionsButton);
        commentButton = findViewById(R.id.commentButton);
        ingredientsButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);

        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fabFavorite.setOnClickListener(this);

        fabRate = (FloatingActionButton) findViewById(R.id.fabRate);
        fabRate.setOnClickListener(this);

        textViewRecipe = findViewById(R.id.textViewRecipe);
        textViewTags = findViewById(R.id.textViewTags);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTime = findViewById(R.id.textViewTime);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        commentText = findViewById(R.id.commentText);

        imageview = findViewById(R.id.imageView);

        commentList = (ListView) findViewById(R.id.commentList);

         db = FirebaseFirestore.getInstance();
         recipe = new Recipe();

        getRecipe();

    }


    @Override
    public void onClick(View view) {
        if (view == ingredientsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
            instructionsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
            ViewCompat.setBackgroundTintList(ingredientsButton,getResources().getColorStateList(R.color.colorPrimary) );
            ViewCompat.setBackgroundTintList(instructionsButton,getResources().getColorStateList(R.color.colorWhite) );

            textViewRecipe.setText(recipe.getIngredients());
        }
        else if (view == instructionsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
            instructionsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
            ViewCompat.setBackgroundTintList(ingredientsButton,getResources().getColorStateList(R.color.colorWhite) );
            ViewCompat.setBackgroundTintList(instructionsButton,getResources().getColorStateList(R.color.colorPrimary) );

            textViewRecipe.setText(recipe.getInstructions());
        }
        else if (view == fabFavorite){
            favorite();
        }
        else if (view == fabRate){
            rate();
        }
        else if (view == commentButton)
        {
            addComment();
            commentText.setText("");
            try  {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }

        }
    }

    void favorite() {
        //lisää resepti favorite listalle
    }

    void rate() {
        //arvostele resepti
    }

    void getRecipe(){

        DocumentReference docRef = db.collection("recipes").document("Burger");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recipe = documentSnapshot.toObject(Recipe.class);
                textViewRecipe.setText(recipe.getIngredients());
                textViewTags.setText(recipe.getTags());
                textViewTitle.setText(recipe.getTitle());
                textViewAuthor.setText(recipe.getUsername());
                textViewTime.setText(recipe.getTime());
                stars = recipe.getStars();

                switch (stars){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;

                        default:
                            break;
                }
                Glide.with(getApplicationContext()).load(recipe.getImage()).into(imageview);

                comments = recipe.getComments();
               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecipeView.this, android.R.layout.simple_list_item_1, comments );
               //arrayAdapter = new commentArrayAdapter(RecipeView.this, comments);
                commentList.setAdapter(arrayAdapter);
                Utility.setListViewHeightBasedOnChildren(commentList);

            }
        });
    }

    void addComment(){
        String comment = commentText.getText().toString();
        if(comment.length() != 0 ) {
            comments = recipe.getComments();
            comments.add(comment);

            recipe.setComments(comments);
            //recipe.setId(recipe.getId());
            //recipe.setTitle(recipe.getTitle());
            //recipe.setTags(recipe.getTags());
            //recipe.setIngredients(recipe.getIngredients());
            //recipe.setInstructions(recipe.getInstructions());
            //recipe.setTime(recipe.getTime());
            //recipe.setUsername(recipe.getUsername());
            db.collection("recipes").document(recipe.getId()).set(recipe);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecipeView.this, android.R.layout.simple_list_item_1, comments);
            //arrayAdapter = new commentArrayAdapter(RecipeView.this, comments);
            commentList.setAdapter(arrayAdapter);
            Utility.setListViewHeightBasedOnChildren(commentList);
        }

    }

}
