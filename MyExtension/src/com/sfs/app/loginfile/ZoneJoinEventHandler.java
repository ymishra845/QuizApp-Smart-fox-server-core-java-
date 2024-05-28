package com.sfs.app.loginfile;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;


import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ZoneJoinEventHandler extends BaseServerEventHandler {

	
    @Override
    public void handleServerEvent(ISFSEvent event) {
        User user = (User) event.getParameter(SFSEventParam.USER);

        trace("inside zoneJoin event");
        if (user != null) {
            ISFSObject quizData = getQuizData(user.getName());
            trace(quizData.getDump());
            if (quizData != null) {
                // Send quiz data to the client
                getApi().sendExtensionResponse("quizDataCommand", quizData, user, null, false);
               
                trace("data sent to client");
            }
        }
    }

    private ISFSObject getQuizData(String username) {
        ISFSObject quizData = new SFSObject();
        ISFSArray quizArray = new SFSArray();

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/user_login";
        String user = "root";
        String password = "root";

        // JDBC variables
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            connection = DriverManager.getConnection(url, user, password);

            // Prepare SQL query
            String query = "SELECT * FROM user_history WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // Execute query
            resultSet = statement.executeQuery();
            trace("query executed");
            
            // Iterate through results and add them to the array
            while (resultSet.next()) {
                ISFSObject quizRecord = new SFSObject();
                int quizId = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quiz_name");
                int score = resultSet.getInt("score");
                String Date_time = resultSet.getString("Date_time");

                quizRecord.putInt("quizId", quizId);
                quizRecord.putUtfString("quizName", quizName);
                quizRecord.putInt("score", score);
                quizRecord.putUtfString("Date_time", Date_time);

                quizArray.addSFSObject(quizRecord);
            }

            // Add the array to the main quizData object
            quizData.putSFSArray("quizRecords", quizArray);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return quizData;
    }
}