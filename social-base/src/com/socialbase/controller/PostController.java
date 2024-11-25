package com.socialbase.controller;

import com.socialbase.model.Post;
import com.socialbase.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostController {
    private Map<Integer, Post> posts = new HashMap<>(); // Simulating a database with a HashMap
    private Map<Integer, User> users = new HashMap<>(); // Simulating a database with a HashMap for users
    private int postIdCounter = 1; // To generate unique post IDs
    private int userIdCounter = 1; // To generate unique user IDs

    // Create a new post
    public Post createPost(int userId, String content) {
        Post newPost = new Post(postIdCounter++, userId, content, new Timestamp(System.currentTimeMillis()));
        posts.put(newPost.getPostId(), newPost);
        return newPost;
    }


    // Retrieve all posts
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts.values());
    }

    // Implement the loginUser  method
    public User loginUser (String username, String password) {
        for (User  user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // User found and authenticated
            }
        }
        return null; // Authentication failed
    }

    // Implement the registerUser  method
    public User registerUser (String username, String password, String email) {
        // Create a new user
        User newUser  = new User(userIdCounter++, username, password, email);
        users.put(newUser .getUserId(), newUser ); // Store the new user in the users map
        return newUser ; // Return the newly created user
    }
    public List<Post> getPostsByUserId(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPostsByUserId'");
    }
}