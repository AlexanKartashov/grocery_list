package com.example.grocerylisting.Models;

import android.view.View;
import android.widget.TextView;

public class Recipe {

    public String key;
    private String tile;
    private String instruction;
    private String picture;
    private String author;

    public Recipe() {
    }

    public Recipe(String tile, String instruction, String author,
                  String picture) {
        this.tile = tile;
        this.instruction = instruction;
        this.picture = picture;
        this.author = author;
    }

    public Recipe(TextView tile, TextView instruction, TextView author,
                  String picture) {
        this.tile = tile.getText().toString();
        this.instruction = instruction.getText().toString();
        this.picture = picture;
        this.author = author.getText().toString();
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
