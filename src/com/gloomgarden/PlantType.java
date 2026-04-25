package com.gloomgarden;

public enum PlantType {
    GENERATOR(10, "Generator"),
    ROSE(15, "Rose"),
    FLYTRAP(20, "Flytrap"),
    IVY(5, "Ivy");

    public final int cost;
    public final String name;

    PlantType(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }
}
