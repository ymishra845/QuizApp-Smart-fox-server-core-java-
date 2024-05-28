package com.sfs.app.loginfile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginReqEventHandler extends BaseServerEventHandler {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/user_login"; // Replace 'database_name' with your database name
        String user = "root"; // Replace 'your_username' with your MySQL username
        String password = "root"; // Replace 'your_password' with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }
    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        User user = (User) event.getParameter(SFSEventParam.USER);
        String username = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
        ISFSObject sfsoIn = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA); // Corrected here
        String password = sfsoIn.getUtfString("password");
        
        trace(username+"  "+ password);
        
        

        // Authenticate user
        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            // Login successful
            trace("Login Success for user: " + username);
            
        } else {
            // Failed to authenticate, try to register the user
            trace("Registering user: " + username);
            boolean isRegistered = registerUser(username, password);
            if (isRegistered) {
                // User registered successfully, now login
                trace("User registered successfully. Logging in user: " + username);
            } else {
                // Failed to register user
                trace("Failed to register user: " + username);
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT password FROM login_details WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    // Compare the provided password with the stored password directly
                    return password.equals(storedPassword);
                } else {
                    return false; // No user found with the given username
                }
            }
        } catch (SQLException e) {
            trace("Error authenticating user", e);
            return false;
        }
    }


    private boolean registerUser(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO login_details (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // Return true if the user was registered successfully
        } catch (SQLException e) {
            trace("Error registering user", e);
            return false;
        }
    }

}
