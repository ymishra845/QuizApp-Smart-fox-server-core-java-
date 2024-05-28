package com.sfs.app;

import java.util.concurrent.TimeUnit;

public class GameController {
    public enum GameState {
        START_QUIZ,
        ACTIVE,
        END
    }

    private GameState gameState = GameState.START_QUIZ;
    private final GameExtension extension;

    public GameController(GameExtension gameExtension) {
        this.extension = gameExtension;
    }

    public void startQuiz() {
        transitionToState(GameState.START_QUIZ);
    }

    public void setGameState(GameState state) {
        this.gameState = state;
        extension.trace("Game state set to: " + state);
    }

    public void transitionToState(GameState nextState) {
        setGameState(nextState);

        switch (nextState) {
            case START_QUIZ:
                extension.trace("Initializing quiz...");
                extension.quizInProgress = true;
               // extension.currentQuestionIndex = 0; 
                scheduleStateTransition(GameState.ACTIVE, 2); // Transition to ACTIVE after 3 seconds
                break;

            case ACTIVE:
                extension.trace("Quiz is active. Sending next question...");
                extension.sendNextQuestion();
                break;

            case END:
                extension.trace("Quiz ended. Sending final marks...");
                extension.sendMarks();
                break;

            default:
                extension.trace("Unknown state detected!");
                break;
        }
    }

    public void scheduleStateTransition(GameState nextState, long delay) {
        TaskSchedulerPool.scheduleTask(() -> transitionToState(nextState), delay, TimeUnit.SECONDS);
    }
}
