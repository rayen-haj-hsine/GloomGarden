package com.gloomgarden;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton btnEndTurn;

    private final Color BG_COLOR = new Color(25, 25, 35);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color ACCENT_COLOR = new Color(0, 180, 180);
    private final Color BTN_DEFAULT = new Color(45, 45, 55);
    private final Color BTN_HOVER = new Color(65, 65, 75);
    private final Color BTN_ACTIVE = new Color(80, 160, 100);

    public UIManager(GameManager game, GamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(10, 15, 15, 15));
        
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        
        lblEnergy = createStyledLabel("⚡ Energy: " + game.getEnergy(), new Color(255, 215, 0));
        lblOriginHealth = createStyledLabel("❤️ Origin HP: " + game.getOriginHealth(), new Color(255, 80, 80));
        
        statsPanel.add(lblEnergy);
        statsPanel.add(lblOriginHealth);
        
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new GridLayout(2, 3, 10, 10));
        actionPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        btnExpand = createStyledButton("Expand (5E)");
        btnExpand.addActionListener(e -> toggleAction(GameManager.PlayerAction.EXPAND));
        
        btnGen = createStyledButton("Generator (10E)");
        btnGen.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_GENERATOR));
        
        btnRose = createStyledButton("Rose (15E)");
        btnRose.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_ROSE));
        
        btnFlytrap = createStyledButton("Flytrap (20E)");
        btnFlytrap.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_FLYTRAP));
        
        btnIvy = createStyledButton("Ivy (5E)");
        btnIvy.addActionListener(e -> toggleAction(GameManager.PlayerAction.PLANT_IVY));
        
        btnEndTurn = createStyledButton("End Turn");
        btnEndTurn.setBackground(new Color(180, 60, 60)); // Special color for end turn
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
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        lblStatus.setForeground(new Color(180, 180, 200));

        add(statsPanel, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }
    
    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(color);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(BTN_DEFAULT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() == BTN_DEFAULT) {
                    btn.setBackground(BTN_HOVER);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() == BTN_HOVER) {
                    btn.setBackground(BTN_DEFAULT);
                }
            }
        });
        return btn;
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
        lblEnergy.setText("⚡ Energy: " + game.getEnergy());
        lblOriginHealth.setText("❤️ Origin HP: " + game.getOriginHealth());
        lblStatus.setText("Status: " + game.getStatusMessage());
        
        // Highlight active action
        GameManager.PlayerAction current = game.getCurrentAction();
        
        btnExpand.setBackground(current == GameManager.PlayerAction.EXPAND ? BTN_ACTIVE : BTN_DEFAULT);
        btnGen.setBackground(current == GameManager.PlayerAction.PLANT_GENERATOR ? BTN_ACTIVE : BTN_DEFAULT);
        btnRose.setBackground(current == GameManager.PlayerAction.PLANT_ROSE ? BTN_ACTIVE : BTN_DEFAULT);
        btnFlytrap.setBackground(current == GameManager.PlayerAction.PLANT_FLYTRAP ? BTN_ACTIVE : BTN_DEFAULT);
        btnIvy.setBackground(current == GameManager.PlayerAction.PLANT_IVY ? BTN_ACTIVE : BTN_DEFAULT);
        
        // Ensure End Turn stays its normal red color
        btnEndTurn.setBackground(new Color(180, 60, 60));
    }
}
