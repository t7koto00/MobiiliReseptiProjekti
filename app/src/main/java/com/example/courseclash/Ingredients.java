package com.example.courseclash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Ingredients extends AppCompatActivity implements View.OnClickListener {

    public ImageView imageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        imageView = findViewById(R.id.imageViewIngredient);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == imageView){
            imageView.setImageResource(R.drawable.ingredientlistclicked);
        }
    }
}
