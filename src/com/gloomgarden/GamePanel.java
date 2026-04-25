package com.gloomgarden;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    private GameManager game;
    private final int TILE_SIZE = 40;

    public GamePanel(GameManager game) {
        this.game = game;
        int size = game.getGridManager().getSize() * TILE_SIZE;
        setPreferredSize(new Dimension(size, size));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tx = e.getX() / TILE_SIZE;
                int ty = e.getY() / TILE_SIZE;
                game.handleTileClick(tx, ty);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        GridManager grid = game.getGridManager();
        int size = grid.getSize();
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                drawTile(g, t, x * TILE_SIZE, y * TILE_SIZE);
            }
        }
    }

    private void drawTile(Graphics g, Tile t, int px, int py) {
        // Tile background
        switch (t.getState()) {
            case DARK:
                g.setColor(new Color(20, 20, 30));
                break;
            case LIT:
                g.setColor(new Color(60, 60, 80));
                break;
            case OWNED:
                g.setColor(new Color(40, 150, 60));
                break;
            case INFESTED:
                g.setColor(new Color(100, 40, 40));
                break;
            case CLEARED:
                g.setColor(new Color(80, 80, 80));
                break;
        }
        g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(px, py, TILE_SIZE, TILE_SIZE);

        // Danger indicator on lit tiles
        if (t.getState() == TileState.LIT || t.getState() == TileState.INFESTED) {
            int danger = t.getDangerTimer();
            if (danger > 0) {
                g.setColor(Color.RED);
                g.drawString(String.valueOf(danger), px + 5, py + 15);
            }
        }

        // Draw plants
        if (!t.getPlants().isEmpty()) {
            for (int i = 0; i < t.getPlants().size(); i++) {
                Plant p = t.getPlants().get(i);
                g.setColor(p.getType().color);
                int offX, offY;
                if (t.getPlants().size() <= 3) {
                    offX = px + 5 + (i * 10);
                    offY = py + 25;
                } else {
                    int row = i / 3;
                    int col = i % 3;
                    offX = px + 4 + (col * 11);
                    offY = py + 4 + (row * 11);
                }
                g.fillRect(offX, offY, 8, 8);
                g.setColor(Color.WHITE);
                g.drawString(p.getType().name.substring(0,1), offX+1, offY+8);
            }
        }

        // Draw pests
        if (!t.getPests().isEmpty()) {
            g.setColor(Color.RED);
            g.fillOval(px + 20, py + 5, 10, 10);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(t.getPests().size()), px + 22, py + 15);
        }
        
        // Origin marker
        GridManager grid = game.getGridManager();
        if (t.getX() == grid.getSize()/2 && t.getY() == grid.getSize()/2) {
            g.setColor(Color.CYAN);
            g.drawRect(px+2, py+2, TILE_SIZE-4, TILE_SIZE-4);
        }
    }
}
