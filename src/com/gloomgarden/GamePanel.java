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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GridManager grid = game.getGridManager();
        int size = grid.getSize();
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile t = grid.getTile(x, y);
                drawTile(g, t, x * TILE_SIZE, y * TILE_SIZE);
            }
        }
        
        if (game.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fm = g.getFontMetrics();
            String msg = "GAME OVER";
            int msgX = (getWidth() - fm.stringWidth(msg)) / 2;
            int msgY = getHeight() / 2;
            g.drawString(msg, msgX, msgY);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            fm = g2d.getFontMetrics();
            String subMsg = "Origin Plant Destroyed!";
            int subMsgX = (getWidth() - fm.stringWidth(subMsg)) / 2;
            int subMsgY = msgY + 40;
            g2d.drawString(subMsg, subMsgX, subMsgY);
        }
    }

    private void drawTile(Graphics g, Tile t, int px, int py) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Tile background
        Color topColor, bottomColor;
        switch (t.getState()) {
            case DARK:
                topColor = new Color(20, 20, 30); bottomColor = new Color(10, 10, 15);
                break;
            case LIT:
                topColor = new Color(70, 70, 90); bottomColor = new Color(50, 50, 70);
                break;
            case OWNED:
                topColor = new Color(50, 180, 80); bottomColor = new Color(30, 120, 50);
                break;
            case INFESTED:
                topColor = new Color(130, 40, 40); bottomColor = new Color(80, 20, 20);
                break;
            case CLEARED:
                topColor = new Color(90, 90, 90); bottomColor = new Color(60, 60, 60);
                break;
            default:
                topColor = Color.BLACK; bottomColor = Color.BLACK;
        }
        g2d.setPaint(new GradientPaint(px, py, topColor, px, py + TILE_SIZE, bottomColor));
        g2d.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        g2d.setColor(new Color(255, 255, 255, 20));
        g2d.drawRect(px, py, TILE_SIZE, TILE_SIZE);

        // Danger indicator on lit tiles
        if (t.getState() == TileState.LIT || t.getState() == TileState.INFESTED) {
            int danger = t.getDangerTimer();
            if (danger > 0) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.valueOf(danger), px + 5, py + 15);
            }
        }

        // Draw plants
        if (!t.getPlants().isEmpty()) {
            for (int i = 0; i < t.getPlants().size(); i++) {
                Plant p = t.getPlants().get(i);
                g2d.setColor(p.getType().color);
                int offX, offY;
                if (t.getPlants().size() <= 3) {
                    offX = px + 6 + (i * 12);
                    offY = py + 24;
                } else {
                    int row = i / 3;
                    int col = i % 3;
                    offX = px + 6 + (col * 11);
                    offY = py + 6 + (row * 11);
                }

                int s = 8;
                switch (p.getType()) {
                    case GENERATOR:
                        g2d.fillOval(offX, offY, s, s);
                        g2d.setColor(new Color(255, 255, 255, 150));
                        g2d.fillOval(offX+2, offY+2, s-4, s-4);
                        break;
                    case ROSE:
                        int[] rx = {offX+s/2, offX+s, offX+s/2, offX};
                        int[] ry = {offY, offY+s/2, offY+s, offY+s/2};
                        g2d.fillPolygon(rx, ry, 4);
                        break;
                    case FLYTRAP:
                        g2d.fillArc(offX, offY, s, s, 45, 270);
                        break;
                    case IVY:
                        g2d.fillRect(offX+s/2-1, offY, 2, s);
                        g2d.fillRect(offX, offY+s/2-1, s, 2);
                        break;
                }
            }
        }

        // Draw pests
        if (!t.getPests().isEmpty()) {
            g2d.setColor(new Color(255, 50, 50));
            int cx = px + 25;
            int cy = py + 10;
            int[] pxPts = {cx, cx+5, cx+10, cx+5, cx, cx-5, cx-10, cx-5};
            int[] pyPts = {cy-10, cy-5, cy, cy+5, cy+10, cy+5, cy, cy-5};
            g2d.fillPolygon(pxPts, pyPts, 8);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString(String.valueOf(t.getPests().size()), cx - 3, cy + 4);
        }
        
        // Origin marker
        GridManager grid = game.getGridManager();
        if (t.getX() == grid.getSize()/2 && t.getY() == grid.getSize()/2) {
            g2d.setColor(new Color(0, 255, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(px+1, py+1, TILE_SIZE-2, TILE_SIZE-2);
            g2d.setStroke(new BasicStroke(1));
        }
    }
}
