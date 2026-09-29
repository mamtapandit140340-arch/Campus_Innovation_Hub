package com.campushub.model;

public class IdeaTechnology {

    private int ideaId;
    private String technology;

    public IdeaTechnology() {
    }

    // Constructor for CREATE
    public IdeaTechnology(int ideaId, String technology) {
        this.ideaId = ideaId;
        this.technology = technology;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
