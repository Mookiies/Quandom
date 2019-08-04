package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

final public class GameModel implements Serializable {

    private static final long serialVersionUID = 1111111112L;

    private ArrayList<Question> questions;
    private ArrayList<Player> players;
    private int currentQuestionIdx = 0;
    private int firstGuessingPlayer = 0;
    private int currentGuessingPlayer = 0;
    private boolean isGameOver = false;
    private int category;
    private String difficulty;
    private boolean usedCache;

    private HashMap<Integer, Integer> playerIdxToGuessIdx;

    public GameModel(ArrayList<Player> players, String response,
                     int category, String difficulty, boolean usedCache) {
        this.players = players;
        this.category = category;
        this.difficulty = difficulty;
        playerIdxToGuessIdx =  new HashMap<>();
        this.usedCache = usedCache;

        // TODO randomize which player goes first the first time

        Log.d("RESPONSE", response);
        this.questions = new ArrayList<>();
        this.populateQuestions(response);
    }

    private void populateQuestions(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONArray ques = json.getJSONArray("results");
            for (int i = 0; i < ques.length(); i++) {
                JSONObject question = ques.getJSONObject(i);
                String category = unescapeHtml4(question.getString("category"));
                String questionText = unescapeHtml4(question.getString("question"));
                String correctAns = unescapeHtml4(question.getString("correct_answer"));
                JSONArray ans = question.getJSONArray("incorrect_answers");
                Random rand = new Random();
                int correctIdx = rand.nextInt(4);
                ArrayList<String> answers = new ArrayList<>();

                for (int j = 0; j < ans.length(); j++) {
                    answers.add(unescapeHtml4(ans.getString(j)));
                }
                answers.add(correctIdx, correctAns);
                Question questionObj = new Question(questionText, answers, correctIdx);
                this.questions.add(questionObj);
                Log.d("ANS", "ques: " + questionObj.toString());

            }
        } catch (JSONException e) {
            Log.d("JSONOBJ", e.getMessage());
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
        return new ArrayList<>(players);
    }

    /**
     * Sorted by natural ordering of Player's comparable. Least to greatest.
     */
    public ArrayList<Player> getSortedPlayers() {
        ArrayList<Player> list = new ArrayList<>(players);
        Collections.sort(list);
        return list;
    }

    public ArrayList<Player> getSortedPlayersByWins() {
        ArrayList<Player> list = new ArrayList<>(players);
        Collections.sort(list, new SortByWins());
        return list;
    }

    class SortByWins implements Comparator<Player> {

        @Override
        public int compare(Player player1, Player player2) {
            return player1.getPlayerWins() - player2.getPlayerWins();
        }
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public ArrayList<Player> getWinningPlayer() {
        ArrayList<Player> winners = new ArrayList<>();
        Player highestPlayer = Collections.max(players);
        for (Player player : players) {
            if (player.getPlayerScore() == highestPlayer.getPlayerScore()) {
                winners.add(player);
            }
        }
        return winners;
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
                currentQuestion.addCorrectGuessingPlayer(player);
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

    public int getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean isUsedCache() {
        return usedCache;
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
