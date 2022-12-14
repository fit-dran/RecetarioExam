package com.example.recetario.models;

public class Recipe {
    private String name;
    private String description;
    private String ingredients;
    private String instructions;

    public Recipe() {
    }

    public Recipe(String name, String description, String ingredients, String preparation) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = preparation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
