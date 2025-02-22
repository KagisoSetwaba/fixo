package com.socialbase.controller;

import com.socialbase.model.Post;
import com.socialbase.model.User;
import com.socialbase.db.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private DatabaseConnector databaseConnector;
    private Map<Integer, User> users; // Declare the users map
    private int userIdCounter = 1; // Declaration and initialization of userIdCounter

    public UserController() {
        this.databaseConnector = new DatabaseConnector();
        this.users = new HashMap<>(); // Initialize the users map
    }

    public User registerUser (String username, String password, String email) {
        String checkUserQuery = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery)) {
    
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();
    
            if (resultSet.next()) {
                return null; // Username already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle exception
        }
    
        User newUser  = new User(userIdCounter++, username, password, email);
        String insertUserQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertUserQuery)) {
    
            insertStatement.setString(1, newUser .getUsername());
            insertStatement.setString(2, newUser .getPassword()); // Password should be hashed
            insertStatement.setString(3, newUser .getEmail());
            insertStatement.executeUpdate();
    
            users.put(newUser .getUserId(), newUser );
            return newUser ;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle exception
        }
    }

    // Login a user
    public User loginUser (String username, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password); // Note: Password should be hashed in a real application

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String userUsername = resultSet.getString("username");
                String userPassword = resultSet.getString("password");
                String email = resultSet.getString("email"); // Assuming you have an email field

                user = new User(userId, userUsername, userPassword, email);
                System.out.println("Login successful for user: " + username); // Debug statement
            } else {
                System.out.println("Login failed for user: " + username); // Debug statement
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user; // Returns null if authentication failed
    }

    // Update user information
    public boolean updateUser (int userId, String username, String password, String email) {
        String updateUserQuery = "UPDATE users SET username = ?, password = ?, email = ? WHERE user_id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateUserQuery)) {

            updateStatement.setString(1, username);
            updateStatement.setString(2, password); // Note: Password should be hashed in a real application
            updateStatement.setString(3, email);
            updateStatement.setInt(4, userId);

            int rowsAffected = updateStatement.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle exception
        }
    }

    // Get user by ID
    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                user = new User(userId, username, password, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Returns null if user not found
    }

    // Get posts by user ID
    public List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts WHERE user_id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int postId = resultSet.getInt("post_id");
                String content = resultSet.getString("content");
                // Assuming you have a Post constructor that takes these parameters
                Post post = new Post(postId, userId, content, null);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts; // Returns an empty list if no posts found
    }
}