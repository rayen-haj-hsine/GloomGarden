package com.gloomgarden;

import javax.swing.*;
import java.awt.*;

public class UIManager extends JPanel {
    private GameManager game;
    private GamePanel gamePanel;
    private JLabel lblEnergy;
    private JLabel lblOriginHealth;
    private JLabel lblStatus;

    private JButton btnExpand;
    private JButton btnGen;
    private JButton btnRose;
    private JButton btnFlytrap;
    private JButton btnIvy;

    public UIManager(GameManager game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout());
        
        JPanel statsPanel = new JPanel();
        lblEnergy = new JLabel("Energy: " + game.getEnergy());
        lblOriginHealth = new JLabel("Origin HP: " + game.getOriginHealth());
        statsPanel.add(lblEnergy);
        statsPanel.add(new JLabel(" | "));
        statsPanel.add(lblOriginHealth);
        
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(2, 3, 5, 5));
        
        btnExpand = new JButton("Expand (5E)");
        btnExpand.addActionListener(e -> toggleAction(GameManager.PlayerAction.EXPAND));
        
        btnGen = new JButton("Generator (10E)");
        btnGen.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_GENERATOR));
        
        btnRose = new JButton("Rose (15E)");
        btnRose.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_ROSE));
        
        btnFlytrap = new JButton("Flytrap (20E)");
        btnFlytrap.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_FLYTRAP));
        
        btnIvy = new JButton("Ivy (5E)");
        btnIvy.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_IVY));
        
        JButton btnEndTurn = new JButton("End Turn");
        btnEndTurn.setBackground(new Color(200, 100, 100));
        btnEndTurn.addActionListener(e -> {
            game.endTurn();
            updateUIState();
            gamePanel.repaint();
        });

        actionPanel.add(btnExpand);
        actionPanel.add(btnGen);
        actionPanel.add(btnRose);
        actionPanel.add(btnFlytrap);
        actionPanel.add(btnIvy);
        actionPanel.add(btnEndTurn);

        lblStatus = new JLabel("Status: " + game.getStatusMessage(), SwingConstants.CENTER);

        add(statsPanel, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }
    
    private void toggleAction(GameManager.PlayerAction action) {
        if (game.getCurrentAction() == action) {
            game.setCurrentAction(GameManager.PlayerAction.NONE);
            game.setStatusMessage("Action deselected.");
        } else {
            game.setCurrentAction(action);
            game.setStatusMessage("Action selected: " + action.name() + ". Click a tile.");
        }
        updateUIState();
    }

    public void updateUIState() {
        lblEnergy.setText("Energy: " + game.getEnergy());
        lblOriginHealth.setText("Origin HP: " + game.getOriginHealth());
        lblStatus.setText("Status: " + game.getStatusMessage());
        
        // Highlight active action
        GameManager.PlayerAction current = game.getCurrentAction();
        Color defaultColor = new JButton().getBackground();
        Color activeColor = new Color(150, 200, 255);
        
        btnExpand.setBackground(current == GameManager.PlayerAction.EXPAND ? activeColor : defaultColor);
        btnGen.setBackground(current == GameManager.PlayerAction.PLANT_GENERATOR ? activeColor : defaultColor);
        btnRose.setBackground(current == GameManager.PlayerAction.PLANT_ROSE ? activeColor : defaultColor);
        btnFlytrap.setBackground(current == GameManager.PlayerAction.PLANT_FLYTRAP ? activeColor : defaultColor);
        btnIvy.setBackground(current == GameManager.PlayerAction.PLANT_IVY ? activeColor : defaultColor);
    }
}
