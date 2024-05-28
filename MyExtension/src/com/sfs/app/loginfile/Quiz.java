package com.sfs.app.loginfile;

import java.util.ArrayList;

public class Quiz {

    private final ArrayList<String> questions;

    public Quiz() {
        // Initialize the ArrayList with questions
        questions = new ArrayList<>();
        questions.add(" ");
        questions.add(" ");
        questions.add("Who wrote 'Romeo and Juliet'? A) William Shakespeare B) Jane Austen C) Charles Dickens D) Leo Tolstoy");
        questions.add("What is the capital of india? A) Delhi B) chandigarh C) luckhnow D) haryana");
        questions.add("Which of the following odd one? A) square B) circle C) rectangle D) box");
        questions.add("how many bones are there in human body? A) 209 B) 206 C) 208 D) 207");
        questions.add("What is the capital of France? A) Paris B) Rome C) Madrid D) Berlin");
        questions.add("Which planet is known as the Red Planet? A) Venus B) Mars C) Jupiter D) Saturn");
        questions.add("What is the largest ocean on Earth? A) Atlantic Ocean B) Indian Ocean C) Arctic Ocean D) Pacific Ocean");
        questions.add("Who wrote the play 'Romeo and Juliet'? A) Charles Dickens B) William Shakespeare C) Mark Twain D) Jane Austen");
        questions.add("What is the capital city of Japan? A) Beijing B) Seoul C) Tokyo D) Bangkok");
        questions.add("Which element has the chemical symbol 'O'? A) Oxygen B) Gold C) Silver D) Hydrogen");
        questions.add("Who was the first person to walk on the moon? A) Yuri Gagarin B) Buzz Aldrin C) Neil Armstrong D) Michael Collins");
        // Add more questions here...
    }

    public boolean checkAnswer(int questionNumber, char answer) {
        // Ensure the question number is valid
        if (questionNumber < 0 || questionNumber >questions.size()) {
            System.out.println("Invalid question number.");
            return false;
        }

        // Get the correct answer for the given question
        char correctAnswer = getCorrectAnswer(questionNumber);

        // Check if the provided answer matches the correct answer
        return Character.toUpperCase(answer) == correctAnswer;
    }

    private char getCorrectAnswer(int questionNumber) {
        char correctAnswer = finalAns(questionNumber);
        return Character.toUpperCase(correctAnswer); // Convert to uppercase for comparison
    }

    private char finalAns(int questionNumber) {
        char ans; // No need for default value

        switch (questionNumber) {
            case 0:
                ans = 'A'; // Correct answer for the first question
                break;
            case 1:
                ans = 'B'; // Correct answer for the second question
                break;
            case 2:
                ans = 'A'; // Correct answer for the third question
                break;
            case 3:
                ans = 'A'; // Correct answer for the fourth question
                break;
            case 4:
                ans = 'D'; // Correct answer for the fifth question
                break;
            case 5:
                ans = 'B'; // Correct answer for the sixth question
                break;
            case 6:
                ans = 'A'; // Correct answer for the sixth question
                break;
            case 7:
                ans = 'B'; // Correct answer for the sixth question
                break;
            case 8:
                ans = 'D'; // Correct answer for the sixth question
                break;
            case 9:
                ans = 'B'; // Correct answer for the sixth question
                break;
            case 10:
                ans = 'C'; // Correct answer for the sixth question
                break;
            case 11:
                ans = 'A'; // Correct answer for the sixth question
                break;
            case 12:
                ans = 'C'; // Correct answer for the sixth question
                break;
            // Add more cases for additional questions if needed
            default:
                System.out.println("Invalid question number.");
                ans = '0'; // Return default value for invalid question numbers
                break;
        }

        return ans;
    }


	public ArrayList<String> getQuestions() {
        return questions;
    }

   
}
