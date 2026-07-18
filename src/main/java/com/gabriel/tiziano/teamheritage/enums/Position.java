package com.gabriel.tiziano.teamheritage.enums;

public enum Position {

    GOALKEEPER("Goleiro"),
    DEFENDER("Zagueiro"),
    MIDFIELDER("Meio-campista"),
    FORWARD("Atacante");

    private final String description;

    Position(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}