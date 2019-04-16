package com.example.courseclash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecipeViewAdapter extends ArrayAdapter<Recipe> implements Filterable {

    public ArrayList<Recipe> recipeList;
    public ArrayList<Recipe> recipeListAll = new ArrayList<>();
    public RecipeViewAdapter(Context context, int recipe_list_item, ArrayList<Recipe> list) {
        super(context, 0,list);

        recipeList = list;
        recipeListAll = new ArrayList<>(list);

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
        ImageView recipeImage = convertView.findViewById(R.id.recipe_image);
        Glide.with(getContext()).load(recipe.getImage()).into(recipeImage);
        TextView recipeTime = convertView.findViewById(R.id.recipe_time);
        TextView recipeTag = convertView.findViewById(R.id.recipe_tag);
        recipeText.setText(String.valueOf(recipe.getTitle()));
        recipeTime.setText(String.valueOf(recipe.getTime()));
        recipeTag.setText(String.valueOf(recipe.getTags()));




        return convertView;
    }

   // @androidx.annotation.NonNull
    @Override
    public Filter getFilter() {
        return recipeFilter;
    }
    public Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Recipe> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(recipeListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Recipe item : recipeListAll) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipeList.clear();
            recipeList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
