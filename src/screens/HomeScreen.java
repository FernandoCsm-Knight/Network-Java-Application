package screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.messages.PlayMessage;
import components.ColorPalette;
import components.CustomButton;

public class HomeScreen extends ScreenWidget {

    public HomeScreen() {
        setLayout(new BorderLayout());
        
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(ColorPalette.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorPalette.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("A Senile Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Make a sequence of three!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitleLabel);

        gbc.gridy = 0;
        gridPanel.add(titlePanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setBackground(ColorPalette.LIGHT_GRAY);

        CustomButton howToPlayButton = new CustomButton("How to play");
        howToPlayButton.addActionListener(e -> {
            context.show(navigator, "HowToPlay");
        });

        CustomButton playButton = new CustomButton("Play");
        playButton.addActionListener(e -> {
            client.send(new PlayMessage());
            context.show(navigator, "Lobby");
        });

        buttonPanel.add(howToPlayButton);
        buttonPanel.add(playButton);

        gbc.gridy = 1;
        gridPanel.add(buttonPanel, gbc);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setPreferredSize(new Dimension(800, 70));
        bottomPanel.setBackground(ColorPalette.LIGHT_GRAY);

        JLabel editorLabel = new JLabel("Made by Augusto S. Oliveira | Pedro H. Pena | Fernando C. Maria", SwingConstants.CENTER);
        editorLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        editorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bottomPanel.add(editorLabel);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
