package com.example.recetario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recetario.R;
import com.example.recetario.models.Recipe;

import java.util.ArrayList;

public class RecyclerViewRecipeAdapter extends RecyclerView.Adapter<RecyclerViewRecipeAdapter.ViewHolder> {

    Context context;
    ArrayList<Recipe> recipes;

    public RecyclerViewRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecyclerViewRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        holder.tvRecipeDescription.setText(recipe.getDescription());
        holder.tvRecipeIngredients.setText(recipe.getIngredients());
        holder.tvRecipeInstructions.setText(recipe.getInstructions());
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;
        TextView tvRecipeDescription;
        TextView tvRecipeIngredients;
        TextView tvRecipeInstructions;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            tvRecipeIngredients = itemView.findViewById(R.id.tvRecipeIngredients);
            tvRecipeInstructions = itemView.findViewById(R.id.tvRecipeInstructions);
        }
    }


}
