package com.gloomgarden;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(Runnable onStartGame) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));

        JLabel title = new JLabel("GloomGarden", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setForeground(new Color(100, 255, 100));
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));

        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("Arial", Font.BOLD, 32));
        startBtn.setBackground(new Color(40, 150, 60));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.addActionListener(e -> onStartGame.run());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(startBtn);

        JLabel subtitle = new JLabel("Protect the Origin. Expand your light.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 24));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(subtitle, BorderLayout.SOUTH);
    }
}
