package utils;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

final public class GameModel implements Serializable { //TODO turn into a model class

    private ArrayList<Question> questions;
    private ArrayList<Player> players;
    private int currentQuestion = 0;
    private int firstGuessingPlayer = 0;
    private int currentGuessingPlayer = 0;
    private boolean isGameOver = false;

    public GameModel(int numQuestions, ArrayList<Player> players) {
        this.players = players;

        ArrayList<String> questionTexts = new ArrayList<>();
        ArrayList<ArrayList<String>> answers = new ArrayList<>();
        ArrayList<Integer> correctAnswers = new ArrayList<>();

        questionTexts.add("Which is not a member of the Microsoft Office Suite?");
        ArrayList<String> answers1 = new ArrayList<>();
        answers1.add("Excel");
        answers1.add("Outlook");
        answers1.add("Teams");
        answers1.add("Slides");
        answers.add(answers1);
        correctAnswers.add(3);

        questionTexts.add("In what year did George Washington first take office as president of the United States?");
        ArrayList<String> answers2 = new ArrayList<>();
        answers2.add("1776");
        answers2.add("1783");
        answers2.add("1789");
        answers2.add("1800");
        answers.add(answers2);
        correctAnswers.add(2);

        questionTexts.add("What does the term \"icosahedron\" refer to?");
        ArrayList<String> answers3 = new ArrayList<>();
        answers3.add("a 24-sided polyhedron");
        answers3.add("a 20-sided polyhedron");
        answers3.add("a 15-sided polyhedron");
        answers3.add("a 12-sided polyhedron");
        answers.add(answers3);
        correctAnswers.add(1);

        this.questions = new ArrayList<>();

        for (int i = 0; i < numQuestions; i++) {
            Question question = new Question(questionTexts.get(i), answers.get(i), correctAnswers.get(i));
            this.questions.add(question);
        }
    }

    public Question getCurrentQuestion() {
        if (isGameOver || questions.size() <= currentQuestion) {
            return null;
        }
        return questions.get(currentQuestion);
    }

    public Player getGuessingPlayer() {
        return players.get(currentGuessingPlayer);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public Player getWinningPlayer() {
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        return p1.getPlayerScore() > p2.getPlayerScore() ? p1 : p2;
    }

    public boolean guessQuestion(int guess) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion != null && currentQuestion.isCorrectAnswer(guess)) {
            Player p = players.get(currentGuessingPlayer);
            p.increasePlayerScoreOnce();
            moveToNextQuestion();
            return true;
        } else {
            moveToNextGuessingPlayer();
            return false;
        }
    }

    public boolean moveToNextQuestion() {
        currentQuestion++;
        moveToNextFirstGuessingPlayer();
        currentGuessingPlayer = firstGuessingPlayer;

        if (currentQuestion >= questions.size()) {
            //todo winning logic
            isGameOver = true;
            return false;
        }  else {
            return true;
        }
    }

    private void moveToNextGuessingPlayer() {
        currentGuessingPlayer = currentGuessingPlayer == 1 ? 0 : 1;
    }

    private void moveToNextFirstGuessingPlayer() {
        firstGuessingPlayer = firstGuessingPlayer == 1 ? 0 : 1;

    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "questions=" + questions +
                ", players=" + players +
                ", currentQuestion=" + currentQuestion +
                ", firstGuessingPlayer=" + firstGuessingPlayer +
                ", currentGuessingPlayer=" + currentGuessingPlayer +
                '}';
    }
}
