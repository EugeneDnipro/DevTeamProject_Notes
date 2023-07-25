package com.example.DevTeamProject_Notes.note;

public enum Privacy {
    PRIVATE("Private"),
    PUBLIC("Public");

    public final String label;

    private Privacy(String label) {
        this.label = label;
    }
}
