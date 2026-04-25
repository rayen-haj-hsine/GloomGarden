package com.gloomgarden;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private int x;
    private int y;
    private TileState state;
    private int dangerTimer;
    private List<Plant> plants;
    private List<Pest> pests;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = TileState.DARK;
        this.dangerTimer = 0;
        this.plants = new ArrayList<>();
        this.pests = new ArrayList<>();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public TileState getState() { return state; }
    public void setState(TileState state) { this.state = state; }
    public int getDangerTimer() { return dangerTimer; }
    public void incrementDangerTimer() { this.dangerTimer++; }
    public void resetDangerTimer() { this.dangerTimer = 0; }
    public List<Plant> getPlants() { return plants; }
    public List<Pest> getPests() { return pests; }

    public boolean addPlant(Plant plant) {
        if (plants.size() < 3) {
            plants.add(plant);
            return true;
        }
        return false;
    }

    public boolean hasPlantType(PlantType type) {
        return plants.stream().anyMatch(p -> p.getType() == type);
    }
}
