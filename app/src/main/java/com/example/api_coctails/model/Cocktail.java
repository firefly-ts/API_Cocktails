package com.example.api_coctails.model;

import java.io.Serializable;

public class Cocktail implements Serializable {
    public long getId() {
        return id;
    }

    private long id;
    private String title;
    private String pictureUrl;
    private String category;
    private String instruction;

    public Cocktail() {
    }

    public Cocktail(long id, String title, String pictureUrl, String category, String instruction) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
        this.category = category;
        this.instruction = instruction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
