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

    private int maxPlants;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = TileState.DARK;
        this.dangerTimer = 0;
        this.plants = new ArrayList<>();
        this.pests = new ArrayList<>();
        this.maxPlants = 3;
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
    
    public void setMaxPlants(int max) { this.maxPlants = max; }

    public boolean addPlant(Plant plant) {
        if (plants.size() < maxPlants) {
            plants.add(plant);
            return true;
        }
        return false;
    }

    public boolean hasPlantType(PlantType type) {
        return plants.stream().anyMatch(p -> p.getType() == type);
    }
}
