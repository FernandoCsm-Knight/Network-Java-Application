package app.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.util.regex.Pattern;

import app.components.CustomButton;
import app.services.ScreenWidget;
import app.services.SettingsService;
import common.design.ApplicationFont;
import common.design.ColorPalette;
import common.types.IPVersion;

public class SettingsScreen extends ScreenWidget {

    private JComboBox<String> ipVersionComboBox;
    private JTextField ipAddressField;
    private JTextField portField;

    public SettingsScreen() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ColorPalette.LIGHT_GRAY);

        CustomButton backButton = new CustomButton("Back");
        backButton.addActionListener(e -> context.show(navigator, "Home"));
        headerPanel.add(backButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(ColorPalette.LIGHT_GRAY);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel ipVersionLabel = new JLabel("IP Version:");
        ipVersionLabel.setFont(ApplicationFont.STANDARD_FONT);
        mainPanel.add(ipVersionLabel, gbc);

        gbc.gridx = 1;
        String[] ipVersions = { "Default", "IPv4", "IPv6" };
        ipVersionComboBox = new JComboBox<>(ipVersions);
        ipVersionComboBox.setPreferredSize(new Dimension(200, 25));
        mainPanel.add(ipVersionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ipAddressLabel = new JLabel("IP Address:");
        ipAddressLabel.setFont(ApplicationFont.STANDARD_FONT);
        mainPanel.add(ipAddressLabel, gbc);

        gbc.gridx = 1;
        ipAddressField = new JTextField();
        ipAddressField.setPreferredSize(new Dimension(200, 25));
        mainPanel.add(ipAddressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel portLabel = new JLabel("Port:");
        portLabel.setFont(ApplicationFont.STANDARD_FONT);
        mainPanel.add(portLabel, gbc);

        gbc.gridx = 1;
        portField = new JTextField();
        portField.setPreferredSize(new Dimension(200, 25));
        mainPanel.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        CustomButton submitButton = new CustomButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        mainPanel.add(submitButton, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            IPVersion ipVersion;

            if(ipVersionComboBox.getSelectedItem().equals("Default")) {
                ipVersion = IPVersion.IPv6;
            } else if(ipVersionComboBox.getSelectedItem().equals("IPv4")) {
                ipVersion = IPVersion.IPv4;
            } else {
                ipVersion = IPVersion.IPv6;
            }

            String ipAddress = ipAddressField.getText();
            String portText = portField.getText();

            if (!isValidIPAddress(ipVersion, ipAddress)) {
                JOptionPane.showMessageDialog(SettingsScreen.this, "Invalid IP address for the selected IP version.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!isValidPort(portText)) {
                JOptionPane.showMessageDialog(SettingsScreen.this, "Invalid port number. Please enter an integer between 1 and 65535.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                SettingsService.saveSettings(ipVersion, ipAddress, Integer.parseInt(portText));
                client.close();
                JOptionPane.showMessageDialog(SettingsScreen.this, "Settings saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                context.show(navigator, "Home");
            }
        }

        private boolean isValidIPAddress(IPVersion ipVersion, String ipAddress) {
            boolean response = false;

            if (ipVersion == IPVersion.IPv4) {
                response = Pattern.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b", ipAddress);
            } else if (ipVersion == IPVersion.IPv6) {
                response = Pattern.matches("\\b(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\b", ipAddress);
            }

            return response;
        }

        private boolean isValidPort(String portText) {
            boolean response = false;

            try {
                int port = Integer.parseInt(portText);
                response = port > 0 && port <= 65535;
            } catch (NumberFormatException e) {
                response = false;
            }

            return response;
        }
    }
}
