package com.treacher.runespan.ui;

import com.treacher.butlerplankmaker.ui.Painter;
import com.treacher.runespan.RuneSpan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michael Treacher
 */

public class GUI extends JFrame {

    private final Container contentPane;
    private JPanel centerPanel;
    private JComboBox logComboBox, changeLevelsComboBox, hopOptionComboBox;
    private RuneSpan runeSpan;

    public GUI(RuneSpan runeSpan) {
        setTitle("treach3rs runespan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.contentPane = getContentPane();
        this.runeSpan = runeSpan;
        buildGUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGUI() {
        buildStartButton();
        buildCenterPanel();
        buildGameTypeLabel();
        buildGameTypeComboBox();
        buildHopOptionLabel();
        buildHopOptionComboBox();
        buildChangeLevelsLabel();
        buildChangeLevelsComboBox();
        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    private void buildStartButton() {
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameType = (String) logComboBox.getSelectedItem();
                String hopOption = (String) hopOptionComboBox.getSelectedItem();
                String changeLevelsOption = (String) changeLevelsComboBox.getSelectedItem();
                Painter.startTime = System.currentTimeMillis();
                RuneSpan.GAME_TYPE = gameType;
                RuneSpan.HOP_OPTION = hopOption;
                RuneSpan.CHANGE_LEVELS_OPTION = changeLevelsOption;
                runeSpan.addTasks();
                dispose();
            }
        });
        contentPane.add(startButton, BorderLayout.PAGE_END);
    }

    private void buildCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
    }

    private void buildGameTypeLabel() {
        final JLabel logLabel = new JLabel("Game Type: ");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logLabel);
    }

    private void buildHopOptionLabel() {
        final JLabel logLabel = new JLabel("Island hopping: ");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logLabel);
    }

    private void buildChangeLevelsLabel() {
        final JLabel logLabel = new JLabel("Change levels: ");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logLabel);
    }

    private void buildChangeLevelsComboBox() {
        changeLevelsComboBox = new JComboBox<String>(new String[]{"Yes", "No"});
        changeLevelsComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(changeLevelsComboBox);
        centerPanel.add(Box.createVerticalStrut(15));
    }

    private void buildHopOptionComboBox() {
        hopOptionComboBox = new JComboBox<String>(new String[]{"Hop", "No hop"});
        hopOptionComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(hopOptionComboBox);
        centerPanel.add(Box.createVerticalStrut(15));
    }

    private void buildGameTypeComboBox() {
        logComboBox = new JComboBox<String>(new String[]{"P2P", "F2P"});
        logComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logComboBox);
        centerPanel.add(Box.createVerticalStrut(15));
    }
}
