package com.gloomgarden;

import java.util.ArrayList;
import java.util.List;

public class GridManager {
    private Tile[][] grid;
    private int size;

    public GridManager(int size) {
        this.size = size;
        grid = new Tile[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Tile(x, y);
            }
        }
    }

    public int getSize() { return size; }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return grid[x][y];
        }
        return null;
    }

    public void updateLighting() {
        // First reset all LIT and CLEARED to DARK (except OWNED/INFESTED)
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].getState() == TileState.LIT || grid[x][y].getState() == TileState.CLEARED) {
                    grid[x][y].setState(TileState.DARK);
                }
            }
        }

        // Apply light from all OWNED tiles (radius 2)
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].getState() == TileState.OWNED) {
                    applyLight(x, y, 2);
                }
            }
        }
        
        // Ensure tiles with pests are INFESTED if they are lit
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid[x][y];
                if (!t.getPests().isEmpty() && t.getState() != TileState.OWNED) {
                    t.setState(TileState.INFESTED);
                }
            }
        }
    }

    private void applyLight(int cx, int cy, int radius) {
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int y = cy - radius; y <= cy + radius; y++) {
                if (Math.abs(x - cx) + Math.abs(y - cy) <= radius) { // Manhattan distance 2
                    Tile t = getTile(x, y);
                    if (t != null && t.getState() == TileState.DARK) {
                        t.setState(TileState.LIT);
                    }
                }
            }
        }
    }

    public List<Tile> getAdjacentTiles(Tile tile) {
        List<Tile> adj = new ArrayList<>();
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        for (int[] d : dirs) {
            Tile t = getTile(tile.getX() + d[0], tile.getY() + d[1]);
            if (t != null) adj.add(t);
        }
        return adj;
    }
}
