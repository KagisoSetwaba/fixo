package com.socialbase.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.socialbase.controller.PostController; // Import PostController
import com.socialbase.model.User;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private PostController postController; // Reference to PostController

    // Constructor that accepts PostController
    public RegistrationFrame(PostController postController) {
        this.postController = postController; // Store the PostController instance

        setTitle("Register");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle registration logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                // Call UserController to register the user
                // Assuming UserController is available in the context
                User newUser  = postController.registerUser (username, password, email);
                if (newUser  != null) {
                    // If successful, open LoginFrame
                    LoginFrame loginFrame = new LoginFrame(postController);
                    loginFrame.setVisible(true);
                    dispose(); // Close registration frame
                } else {
                    JOptionPane.showMessageDialog(RegistrationFrame.this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Email:"));
        add(emailField);
        add(registerButton);
    }
}