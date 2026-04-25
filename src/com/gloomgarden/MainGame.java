package com.gloomgarden;

import javax.swing.*;
import java.awt.*;

public class MainGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GloomGarden - Prototype");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            GameManager gameManager = new GameManager(20); // 20x20 grid
            GamePanel gamePanel = new GamePanel(gameManager);
            com.gloomgarden.UIManager uiManager = new com.gloomgarden.UIManager(gameManager, gamePanel);

            // Timer to update UI continuously based on game state changes outside direct actions if any
            Timer timer = new Timer(100, e -> {
                uiManager.updateUIState();
                gamePanel.repaint();
            });
            timer.start();

            frame.add(gamePanel, BorderLayout.CENTER);
            frame.add(uiManager, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
