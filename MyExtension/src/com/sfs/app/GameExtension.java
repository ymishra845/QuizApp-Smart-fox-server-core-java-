package com.sfs.app;

import com.Util.DatabaseUtils;

import com.sfs.app.loginfile.Quiz;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.concurrent.TimeUnit;

public class GameExtension extends SFSExtension {

	private int gameState = -1; // Initialize gameState to -1
    private Quiz quiz;
    int currentQuestionIndex = 0;
    public boolean quizInProgress = false;
    private int marks = 0;
    public final TaskSchedulerPool taskSchedulerPool = new TaskSchedulerPool();
    private GameController gameController;

    @Override
    public void init() {
        trace("Inside init of GameExtension");
        quiz = new Quiz();
        gameController = new GameController(this);
        
        addEventHandler(SFSEventType.USER_LEAVE_ROOM, LeaveGameHandler.class);
        
        addRequestHandler("game", GameRequestHandler.class);
        gameController.startQuiz();
    }

    public void handleAnswer(User user, String answer,int clientGameStateKey) {
        trace("username= " + user.getName());
        trace("Inside handle answer ");
        trace("Quiz in progress: " + quizInProgress);
        
     // Validate clientGameStateKey
        if (clientGameStateKey != gameState) {
            trace("Client game state key mismatch. Ignoring response.");
            return;
        }
        

        String username = user.getName();
        String quizName = "general_knowledge"; // Default quiz name
        
        if (quizInProgress && currentQuestionIndex >= 0 && currentQuestionIndex < quiz.getQuestions().size()) {
            if (quiz.checkAnswer(currentQuestionIndex, answer.charAt(0))) {
                marks++;
                trace("marks is " + marks);
               
               
            }

            // Cancel the timer if the user answers before the 15 seconds interval
            TaskSchedulerPool.cancelTimer();

            // Increment question index and transition to the next question or end the quiz
            currentQuestionIndex++;
            
            
            gameState++;  // increment gameState
            
            if (currentQuestionIndex >= quiz.getQuestions().size()) {
            	
            	 insertScore(username, quizName, marks);
                gameController.transitionToState(GameController.GameState.END);
                
            } else {
                gameController.transitionToState(GameController.GameState.ACTIVE);
            }
        }
    }

    private void insertScore(String username, String quizName, int score) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        trace("username is " + username);
        trace("Inside insertScore to insert score");

        try {
            connection = DatabaseUtils.getConnection();
            String query = "INSERT INTO user_history (username, quiz_name, score, Date_time) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, "general_knowledge");
            preparedStatement.setInt(3, score);

            // Get current date and time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = formatter.format(new Date());
            preparedStatement.setString(4, currentDateTime);

            preparedStatement.executeUpdate();
            
            trace("Query executed for insert score");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendNextQuestion() {
        trace("Inside sendNextQuestion");
        if (currentQuestionIndex < quiz.getQuestions().size()) {
            String question = quiz.getQuestions().get(currentQuestionIndex);
            sendQuestionToClients(question);

            quizInProgress = true;
            scheduleQuestionTimer();
        } else {
            // No more questions, transition to END state
            gameController.transitionToState(GameController.GameState.END);
        }
    }

    private void scheduleQuestionTimer() {
        trace("Inside scheduleQuestionTimer");
        TaskSchedulerPool.scheduleTask(() -> {
            trace("Timer expired, transitioning to next question");
            currentQuestionIndex++;
            if (currentQuestionIndex >= quiz.getQuestions().size()) {
                gameController.transitionToState(GameController.GameState.END);
            } else {
                gameController.transitionToState(GameController.GameState.ACTIVE);
            }
        }, 15, TimeUnit.SECONDS);
    }

    private void sendQuestionToClients(String question) {
        ISFSObject params = new SFSObject();
        params.putUtfString("question", question);
        send("quiz_question", params, getParentRoom().getUserList());
    }

    public void sendMarks() {
    	int totalMarks=quiz.getQuestions().size()-2;
        ISFSObject params = new SFSObject();
        int wrongAnswer=totalMarks-marks;
        trace("Wrong Answers="+wrongAnswer);
        params.putInt("marks", marks);
        params.putInt("wrongAnswers", wrongAnswer);
        send("quiz_finished", params, getParentRoom().getUserList());
        trace("Wrong Answers: " + params.getInt("wrongAnswers"));
        trace("marks has been sent " + marks);
    }

    public void scheduleStateTransition(GameController.GameState nextState, long delay) {
        gameController.scheduleStateTransition(nextState, delay);
    }

    public void setGameState(GameController.GameState state) {
        gameController.setGameState(state);
    }

    public void reset_Quiz() {
        trace("Resetting quiz...");

        // Reset quiz state
        currentQuestionIndex = 1;
        marks = 0;
        quizInProgress = false;

        // Transition to the START_QUIZ state using GameController
        gameController.transitionToState(GameController.GameState.START_QUIZ);
    }

	
}
