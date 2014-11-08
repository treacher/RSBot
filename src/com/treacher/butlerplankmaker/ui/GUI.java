package com.treacher.butlerplankmaker.ui;

import com.treacher.butlerplankmaker.enums.LogType;
import com.treacher.butlerplankmaker.PlankMaker;

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
    private JComboBox logComboBox;

    public GUI() {
        setTitle("Treach3r's butler plank maker.");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.contentPane = getContentPane();
        buildGUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGUI() {
        buildStartButton();
        buildCenterPanel();
        buildLogLabel();
        buildLogComboBox();
        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    private void buildStartButton() {
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlankMaker.LOG_TYPE = LogType.findById(logComboBox.getSelectedIndex());
                Painter.startTime = System.currentTimeMillis();
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

    private void buildLogLabel() {
        final JLabel logLabel = new JLabel("Log: ");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logLabel);
    }

    private void buildLogComboBox() {
        logComboBox = new JComboBox<String>(new String[]{"Normal logs", "Oak logs", "Teak logs", "Mahogany logs"});
        logComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logComboBox);
        centerPanel.add(Box.createVerticalStrut(15));
    }
}
