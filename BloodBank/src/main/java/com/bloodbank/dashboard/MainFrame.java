package com.bloodbank.dashboard;

import com.bloodbank.auth.AuthenticationService;
import com.bloodbank.auth.InMemoryUserRepository;
import com.bloodbank.auth.LoginPanel;
import com.bloodbank.auth.User;
import com.bloodbank.common.Role;
import org.yourcompany.yourproject.donation.DonationHistoryUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {

        setTitle("Blood Bank Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create CardLayout
        cardLayout = new CardLayout();

        // Create panel that holds all screens
        mainPanel = new JPanel(cardLayout);

        // Create user repository
        InMemoryUserRepository userRepository =
                new InMemoryUserRepository();

        // Create authentication service
        AuthenticationService authenticationService =
                new AuthenticationService(userRepository);

        // Create login screen
        LoginPanel loginPanel =
                new LoginPanel(authenticationService, this);

        // Add login screen
        mainPanel.add(loginPanel, "LOGIN");

        // Add inventory screen
        mainPanel.add(
                new PlaceholderPanel("Inventory — Coming Soon"),
                "INVENTORY"
        );

        // Add donation history screen
        mainPanel.add(
                new DonationHistoryUI(),
                "DONATIONS"
        );

        // Add blood request screen
        mainPanel.add(
                new PlaceholderPanel("Blood Request — Coming Soon"),
                "REQUESTS"
        );

        // Add everything to the JFrame
        add(mainPanel);

        // Show login first
        cardLayout.show(mainPanel, "LOGIN");

        setVisible(true);
    }

    public void showDashboard(User user) {

        if (user.getRole() == Role.ADMIN) {

            AdminDashboard dashboard =
                    new AdminDashboard(this);

            mainPanel.add(dashboard, "ADMIN");

            cardLayout.show(mainPanel, "ADMIN");

        } else if (user.getRole() == Role.HOSPITAL) {

            HospitalDashboard dashboard =
                    new HospitalDashboard(this);

            mainPanel.add(dashboard, "HOSPITAL");

            cardLayout.show(mainPanel, "HOSPITAL");
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showScreen(String screenName) {

        cardLayout.show(mainPanel, screenName);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}