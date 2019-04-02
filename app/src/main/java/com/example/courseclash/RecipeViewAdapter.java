package com.example.courseclash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class RecipeViewAdapter extends ArrayAdapter<Recipe> {

    public ArrayList<Recipe> recipeList;
    public RecipeViewAdapter(Context context, int recipe_list_item, ArrayList<Recipe> list) {
        super(context, 0,list);

        recipeList = list;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        recipeList.get(position);
        Recipe recipe;
        recipe = recipeList.get(position);

        if(convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_list_item, parent, false);

        }
        TextView recipeText = convertView.findViewById(R.id.recipe_title);
        //ImageView recipeImage = convertView.findViewById(R.id.recipe_image);
        TextView recipeTime = convertView.findViewById(R.id.recipe_time);
        TextView recipeTag = convertView.findViewById(R.id.recipe_tag);
        recipeText.setText(String.valueOf(recipe.getTitle()));
        recipeTime.setText(String.valueOf(recipe.getTime()));
        recipeTag.setText(String.valueOf(recipe.getTags()));




        return convertView;
    }
}
