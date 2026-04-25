package com.gloomgarden;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private GridManager gridManager;
    private TurnManager turnManager;
    private int energy;
    private int originHealth;
    private boolean gameOver;
    private String statusMessage;
    
    // Actions: EXPAND, PLANT_GENERATOR, PLANT_ROSE, PLANT_FLYTRAP, PLANT_IVY
    public enum PlayerAction { NONE, EXPAND, PLANT_GENERATOR, PLANT_ROSE, PLANT_FLYTRAP, PLANT_IVY }
    private PlayerAction currentAction = PlayerAction.NONE;

    public GameManager(int gridSize) {
        gridManager = new GridManager(gridSize);
        turnManager = new TurnManager(this);
        energy = 30; // Starting energy
        originHealth = 100;
        gameOver = false;
        statusMessage = "Game Started! Origin plant placed.";

        // Initialize origin
        int center = gridSize / 2;
        Tile origin = gridManager.getTile(center, center);
        origin.setState(TileState.OWNED);
        origin.setMaxPlants(9);
        origin.addPlant(new Plant(PlantType.GENERATOR));
        origin.addPlant(new Plant(PlantType.GENERATOR));
        origin.addPlant(new Plant(PlantType.ROSE));
        origin.addPlant(new Plant(PlantType.FLYTRAP));
        gridManager.updateLighting();
    }

    public GridManager getGridManager() { return gridManager; }
    public int getEnergy() { return energy; }
    public void addEnergy(int amount) { energy += amount; }
    public boolean spendEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }
    public int getOriginHealth() { return originHealth; }
    public void damageOrigin(int amount) {
        originHealth -= amount;
        if (originHealth <= 0) {
            gameOver = true;
            statusMessage = "GAME OVER - Origin Plant Destroyed!";
        }
    }
    public boolean isGameOver() { return gameOver; }
    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String msg) { this.statusMessage = msg; }
    
    public PlayerAction getCurrentAction() { return currentAction; }
    public void setCurrentAction(PlayerAction action) { this.currentAction = action; }

    public void handleTileClick(int x, int y) {
        if (gameOver) return;

        Tile tile = gridManager.getTile(x, y);
        if (tile == null) return;

        switch (currentAction) {
            case EXPAND:
                if ((tile.getState() == TileState.LIT || tile.getState() == TileState.CLEARED || tile.getState() == TileState.INFESTED) && hasAdjacentOwned(tile)) {
                    if (spendEnergy(5)) { // Expand cost
                        tile.setState(TileState.OWNED);
                        gridManager.updateLighting();
                        statusMessage = "Expanded to new tile.";
                    } else {
                        statusMessage = "Not enough energy to expand (Cost: 5).";
                    }
                } else {
                    statusMessage = "Can only expand to lit/adjacent owned tiles.";
                }
                break;
            case PLANT_GENERATOR:
                plantAction(tile, PlantType.GENERATOR);
                break;
            case PLANT_ROSE:
                plantAction(tile, PlantType.ROSE);
                break;
            case PLANT_FLYTRAP:
                plantAction(tile, PlantType.FLYTRAP);
                break;
            case PLANT_IVY:
                plantAction(tile, PlantType.IVY);
                break;
            case NONE:
                statusMessage = "Select an action below.";
                break;
        }
    }
    
    private void plantAction(Tile tile, PlantType type) {
        if (tile.getState() != TileState.OWNED) {
            statusMessage = "Must plant on OWNED tiles.";
            return;
        }
        if (spendEnergy(type.cost)) {
            if (tile.addPlant(new Plant(type))) {
                statusMessage = "Planted " + type.name + ".";
            } else {
                addEnergy(type.cost); // Refund
                statusMessage = "Tile is full.";
            }
        } else {
            statusMessage = "Not enough energy for " + type.name + " (Cost: " + type.cost + ").";
        }
    }

    private boolean hasAdjacentOwned(Tile tile) {
        for (Tile adj : gridManager.getAdjacentTiles(tile)) {
            if (adj.getState() == TileState.OWNED) return true;
        }
        return false;
    }
    
    public void endTurn() {
        if (!gameOver) {
            turnManager.executeTurnPhase();
        }
    }
}
