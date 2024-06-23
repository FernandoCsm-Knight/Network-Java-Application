package app.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.components.CustomButton;
import app.services.ScreenWidget;
import common.design.ApplicationFont;
import common.design.ColorPalette;

public class HowToPlayScreen extends ScreenWidget {
    
    public HowToPlayScreen() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.LIGHT_GRAY);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorPalette.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("How To Play", SwingConstants.CENTER);
        titleLabel.setFont(ApplicationFont.MAIN_TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        gbc.gridy = 0;
        gridPanel.add(titlePanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);

        CustomButton backButton = new CustomButton("Back");
        backButton.addActionListener(e -> {
            context.show(navigator, "Home");
        });

        buttonPanel.add(backButton);

        gbc.gridy = 1;
        gridPanel.add(buttonPanel, gbc);

        add(gridPanel, BorderLayout.CENTER);
        // TODO: Add the rest of the components
    }

}
