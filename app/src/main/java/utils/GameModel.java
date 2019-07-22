package utils;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

final public class GameModel implements Serializable { //TODO turn into a model class

    private static final long serialVersionUID = 1111111112L;

    private ArrayList<Question> questions;
    private ArrayList<Player> players;
    private int currentQuestionIdx = 0;
    private int firstGuessingPlayer = 0;
    private int currentGuessingPlayer = 0;
    private boolean isGameOver = false;

    private HashMap<Integer, Integer> playerIdxToGuessIdx;

    public GameModel(int numQuestions, ArrayList<Player> players) {
        this.players = players;
        playerIdxToGuessIdx =  new HashMap<>();

        // TODO randomize which player goes first the first time

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
        if (isGameOver || questions.size() <= currentQuestionIdx) {
            return null;
        }
        return questions.get(currentQuestionIdx);
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
        //TODO support more players
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        return p1.getPlayerScore() > p2.getPlayerScore() ? p1 : p2;
    }

    public boolean guessQuestion(int guess) {
        playerIdxToGuessIdx.put(currentGuessingPlayer, guess);
        if (playerIdxToGuessIdx.size() == players.size()) { // all players have answered
            grantPlayerScore();
            playerIdxToGuessIdx.clear();
            moveToNextQuestion();
            return true;
        }

        moveToNextGuessingPlayer();
        return false;
    }

    private void grantPlayerScore() {
        Question currentQuestion = questions.get(currentQuestionIdx);
        int correctAnswer = currentQuestion.getCorrectAnswer();
        for (int playerIdx : playerIdxToGuessIdx.keySet()) {
            int guess = playerIdxToGuessIdx.get(playerIdx);
            if (guess == correctAnswer) {
                Player player = players.get(playerIdx);
                player.increasePlayerScoreOnce();
            }
        }
    }

    private void moveToNextQuestion() {
        currentQuestionIdx++;
        if (currentQuestionIdx >= questions.size()) {
            isGameOver = true;
            return;
        }

        firstGuessingPlayer++;
        if (firstGuessingPlayer >= players.size()) {
            firstGuessingPlayer = 0;
        }
        currentGuessingPlayer = firstGuessingPlayer;
    }

    private void moveToNextGuessingPlayer() {
        currentGuessingPlayer++;
        if (currentGuessingPlayer >= players.size()) {
            currentGuessingPlayer = 0;
        }
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "questions=" + questions +
                ", players=" + players +
                ", currentQuestionIdx=" + currentQuestionIdx +
                ", firstGuessingPlayer=" + firstGuessingPlayer +
                ", currentGuessingPlayer=" + currentGuessingPlayer +
                ", isGameOver=" + isGameOver +
                ", playerIdxToGuessIdx=" + playerIdxToGuessIdx +
                '}';
    }
}
