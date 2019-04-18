package com.example.courseclash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
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
    public User user = new User();
    ArrayList<String> comments = new ArrayList<>();
    ListView commentList = null;
    commentArrayAdapter arrayAdapter = null;
    public ImageView imageview = null;
    public ImageView imageStars = null;
    int stars = 0;
    private ProgressBar spinner;
    public LinearLayout linearLayout = null;
    Boolean rated = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        /*LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_recipe, null, false);
        drawer.addView(contentView, 0);*/

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
        imageStars = findViewById(R.id.imageStars);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        commentList = (ListView) findViewById(R.id.commentList);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout.setVisibility(LinearLayout.INVISIBLE);

        Intent intent = getIntent();
        String id = (String) intent.getSerializableExtra("DATA");
         db = FirebaseFirestore.getInstance();
         recipe = new Recipe();

        getRecipe(id);

    }


    @Override
    public void onClick(View view) {
        if (view == ingredientsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
            instructionsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrayStrong, null));
            ViewCompat.setBackgroundTintList(ingredientsButton,getResources().getColorStateList(R.color.colorPrimary) );
            ViewCompat.setBackgroundTintList(instructionsButton,getResources().getColorStateList(R.color.colorWhite) );

            textViewRecipe.setText(recipe.getIngredients());
        }
        else if (view == instructionsButton) {
            ingredientsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGrayStrong, null));
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

        //Väliaikaisesti avaa kehotus kirjautua
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        popDialog.setTitle("Please login to add this to your favorites list");

        popDialog.setPositiveButton(android.R.string.ok,

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                       //vie rekisteröinti sivulel!
                    }
                })

                .setNegativeButton("Cancel",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
        popDialog.create();

        popDialog.show();
    }

    void rate() {
        if(rated == false) {
            final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
            //Creates a new RatingBar and specifies the parameters: setNumStars, setStepSize, setRating
            //ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.ProfileRatingBar);
            //RatingBar rate = new RatingBar(contextThemeWrapper, null, 0);
            final RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(1);
            ratingBar.setRating(5);

            //Creates the layout where the RatingBar will be and sets some of its parameters
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryOld), PorterDuff.Mode.SRC_ATOP);
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams parameters =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT);
            layout.setLayoutParams(parameters);
            layout.setGravity(Gravity.CENTER);
            layout.addView(ratingBar);

            popDialog.setIcon(R.drawable.star);
            popDialog.setTitle("Rate this recipe!");
            popDialog.setView(layout);

// Button OK
            popDialog.setPositiveButton(android.R.string.ok,

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int rating = ratingBar.getProgress();
                            ArrayList<Integer> rateAmount = recipe.getRateAmounts();

                            int rate = rateAmount.get(rating);
                            rate = rate + 1;

                            rateAmount.set(rating, rate);
                            recipe.setRateAmounts(rateAmount);

                            rating = rateAmount.get(0) * 0 + rateAmount.get(1) * 1 + rateAmount.get(2) * 2 + rateAmount.get(3) * 3 + rateAmount.get(4) * 4 + rateAmount.get(5) * 5;
                            Float f = (float) rating / (rateAmount.get(0) + rateAmount.get(1) + rateAmount.get(2) + rateAmount.get(3) + rateAmount.get(4) + rateAmount.get(5));
                            int i = Math.round(f);
                            recipe.setStars(i);
                            db.collection("recipes").document(recipe.getId()).set(recipe);

                            rated = true;

                            dialog.dismiss();

                            Context context = getApplicationContext();
                            CharSequence text = "Thanks for rating!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    })

// ButtonCancel
                    .setNegativeButton("Cancel",

                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });
            popDialog.create();

            popDialog.show();
        }
        else if (rated == true) {
            Context context = getApplicationContext();
            CharSequence text = "You have already rated this recipe!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    void getRecipe(String recipeId){

        DocumentReference docRef = db.collection("recipes").document(recipeId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recipe = documentSnapshot.toObject(Recipe.class);

                textViewRecipe.setText(recipe.getIngredients());
                textViewTags.setText(recipe.getTags());
                textViewTitle.setText(recipe.getTitle());
                textViewAuthor.setText(user.getUserName());
                textViewTime.setText(recipe.getTime());
                stars = recipe.getStars();
                switch (stars){
                    case 0:
                        imageStars.setImageResource(R.drawable.star0);
                        break;
                    case 1:
                        imageStars.setImageResource(R.drawable.star1);
                        break;
                    case 2:
                        imageStars.setImageResource(R.drawable.star2);
                        break;
                    case 3:
                        imageStars.setImageResource(R.drawable.star3);
                        break;
                    case 4:
                        imageStars.setImageResource(R.drawable.star4);
                        break;
                    case 5:
                        imageStars.setImageResource(R.drawable.star5);
                        break;

                        default:
                            break;
                }
                Glide.with(getApplicationContext()).load(recipe.getImage()).into(imageview);

                comments = recipe.getComments();
               arrayAdapter = new commentArrayAdapter(RecipeView.this, comments);
                commentList.setAdapter(arrayAdapter);
                Utility.setListViewHeightBasedOnChildren(commentList, arrayAdapter);
                spinner.setVisibility(View.GONE);
                linearLayout.setVisibility(LinearLayout.VISIBLE);

            }
        });


    }

    void addComment(){
        String comment = commentText.getText().toString();
        if(comment.length() != 0 ) {
            comments = recipe.getComments();
            comments.add(comment);



            recipe.setComments(comments);
            db.collection("comments").document(user.getComments()).set(user);

            arrayAdapter = new commentArrayAdapter(RecipeView.this, comments);
            commentList.setAdapter(arrayAdapter);
            Utility.setListViewHeightBasedOnChildren(commentList, arrayAdapter);
        }

    }
}
