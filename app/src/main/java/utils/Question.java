package utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String questionText;
    private ArrayList<String> answers;
    private int correctAnswer;
    private ArrayList<Player> correctGuessingPlayers;

    Question(String questionText, ArrayList<String> answers, int correctAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.correctGuessingPlayers = new ArrayList<>();
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCorrectAnswerString() {
        return answers.get(correctAnswer);
    }

    public ArrayList<Player> getCorrectGuessingPlayers() {
        return correctGuessingPlayers;
    }

    public void addCorrectGuessingPlayer(Player player) {
        correctGuessingPlayers.add(player);
    }


    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", answers=" + answers +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
