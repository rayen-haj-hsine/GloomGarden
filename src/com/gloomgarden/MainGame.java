package com.gloomgarden;

import javax.swing.*;
import java.awt.*;

public class MainGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GloomGarden - Prototype");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);
            frame.setContentPane(mainPanel);

            MainMenuPanel menuPanel = new MainMenuPanel(() -> {
                // Initialize game on start
                GameManager gameManager = new GameManager(20); // 20x20 grid
                GamePanel gamePanel = new GamePanel(gameManager);
                com.gloomgarden.UIManager uiManager = new com.gloomgarden.UIManager(gameManager, gamePanel);

                JPanel gameContainer = new JPanel(new BorderLayout());
                gameContainer.add(gamePanel, BorderLayout.CENTER);
                gameContainer.add(uiManager, BorderLayout.SOUTH);

                mainPanel.add(gameContainer, "GAME");
                cardLayout.show(mainPanel, "GAME");
                
                // Set fixed size based on game panel
                frame.pack();
                frame.setLocationRelativeTo(null);

                // Timer to update UI continuously
                Timer timer = new Timer(100, e -> {
                    uiManager.updateUIState();
                    gamePanel.repaint();
                });
                timer.start();
            });

            mainPanel.add(menuPanel, "MENU");
            cardLayout.show(mainPanel, "MENU");

            frame.setSize(800, 900); // Initial size for menu
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
