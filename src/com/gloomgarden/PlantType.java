package com.gloomgarden;

import java.awt.Color;

public enum PlantType {
    GENERATOR(10, "Generator", new Color(255, 255, 100)),
    ROSE(15, "Rose", new Color(255, 100, 100)),
    FLYTRAP(20, "Flytrap", new Color(150, 50, 150)),
    IVY(5, "Ivy", new Color(100, 255, 100));

    public final int cost;
    public final String name;
    public final Color color;

    PlantType(int cost, String name, Color color) {
        this.cost = cost;
        this.name = name;
        this.color = color;
    }
}
