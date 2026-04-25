package com.gloomgarden;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private GameManager game;
    private int turnCount;

    public TurnManager(GameManager game) {
        this.game = game;
        this.turnCount = 1;
    }

    public void executeTurnPhase() {
        GridManager grid = game.getGridManager();
        int size = grid.getSize();

        // 1. Gain energy
        int generatedEnergy = 2; // Base energy per turn
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                if (t.getState() == TileState.OWNED) {
                    for (Plant p : t.getPlants()) {
                        if (p.getType() == PlantType.GENERATOR) {
                            generatedEnergy += 10;
                        }
                    }
                }
            }
        }
        game.addEnergy(generatedEnergy);

        // 3. Plants attack (simplistic logic)
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                if (t.getState() == TileState.OWNED) {
                    boolean hasRose = t.hasPlantType(PlantType.ROSE);
                    boolean hasFlytrap = t.hasPlantType(PlantType.FLYTRAP);

                    if (hasRose) {
                        for (Tile adj : grid.getAdjacentTiles(t)) {
                            damagePestsOnTile(adj, 5); // Area damage
                        }
                    }
                    if (hasFlytrap) {
                        for (Tile adj : grid.getAdjacentTiles(t)) {
                            if (!adj.getPests().isEmpty()) {
                                adj.getPests().get(0).takeDamage(15); // Single target high damage
                                break; // Only attacks one pest
                            }
                        }
                    }
                }
            }
        }
        cleanUpDeadPests(grid);

        // 4. Pests move & attack, and Spawning
        // Simple spawning based on danger
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                if ((t.getState() == TileState.LIT || t.getState() == TileState.INFESTED) && t.getState() != TileState.CLEARED) {
                    if (t.getDangerTimer() >= 3) {
                        if (Math.random() < 0.1) { // Reduced spawn chance
                            t.getPests().add(new Pest(5 + t.getDangerTimer())); // Weaker pests
                        }
                    }
                }
            }
        }

        // Pests move towards owned tiles (naive logic: move to adjacent owned, else random)
        List<PestMove> moves = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                List<Pest> toMove = new ArrayList<>(t.getPests());
                for (Pest p : toMove) {
                    boolean attacked = false;
                    for (Tile adj : grid.getAdjacentTiles(t)) {
                        if (adj.getState() == TileState.OWNED) {
                            // Attack
                            if (adj.getX() == size/2 && adj.getY() == size/2) {
                                game.damageOrigin(5); // Damage origin
                            } else {
                                if (!adj.getPlants().isEmpty()) {
                                    adj.getPlants().remove(0); // Destroy a plant
                                } else {
                                    adj.setState(TileState.INFESTED); // Lose tile if no plants
                                    game.damageOrigin(2);
                                }
                            }
                            attacked = true;
                            break;
                        }
                    }
                    if (!attacked) {
                        // Move closer to center roughly
                        Tile next = getNextPestMove(t, grid);
                        if (next != t) {
                            moves.add(new PestMove(p, t, next));
                        }
                    }
                }
            }
        }

        for (PestMove pm : moves) {
            pm.from.getPests().remove(pm.pest);
            pm.to.getPests().add(pm.pest);
        }

        // 5. Danger timer escalation
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                if (t.getState() == TileState.LIT || t.getState() == TileState.INFESTED) {
                    int delay = t.hasPlantType(PlantType.IVY) ? 1 : 2; // Ivy logic
                    boolean nearIvy = false;
                    for (Tile adj : grid.getAdjacentTiles(t)) {
                        if (adj.getState() == TileState.OWNED && adj.hasPlantType(PlantType.IVY)) {
                            nearIvy = true; break;
                        }
                    }
                    if (!nearIvy || turnCount % 2 == 0) { // Slower increase if near ivy
                        t.incrementDangerTimer();
                    }
                } else if (t.getState() == TileState.CLEARED) {
                    t.resetDangerTimer();
                }
            }
        }

        grid.updateLighting();

        turnCount++;
        game.setStatusMessage("Turn " + turnCount + " completed. Earned " + generatedEnergy + " energy.");
    }
    
    private void damagePestsOnTile(Tile t, int amt) {
        for (Pest p : t.getPests()) {
            p.takeDamage(amt);
        }
    }
    
    private void cleanUpDeadPests(GridManager grid) {
        int size = grid.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                t.getPests().removeIf(Pest::isDead);
            }
        }
    }

    private Tile getNextPestMove(Tile current, GridManager grid) {
        int cx = grid.getSize() / 2;
        int cy = grid.getSize() / 2;
        
        List<Tile> adj = grid.getAdjacentTiles(current);
        Tile best = current;
        int minDist = Math.abs(current.getX() - cx) + Math.abs(current.getY() - cy);
        
        for (Tile t : adj) {
            // Only move to lit or infested tiles
            if (t.getState() == TileState.LIT || t.getState() == TileState.INFESTED) {
                int dist = Math.abs(t.getX() - cx) + Math.abs(t.getY() - cy);
                if (dist < minDist) {
                    minDist = dist;
                    best = t;
                }
            }
        }
        return best;
    }

    class PestMove {
        Pest pest;
        Tile from;
        Tile to;
        PestMove(Pest p, Tile f, Tile t) { pest = p; from = f; to = t; }
    }
}
