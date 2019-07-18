package utils;

import java.util.ArrayList;

final public class QuestionsCreator {

    ArrayList<Question> questions;

    public QuestionsCreator(int numQuestions) {

        ArrayList<String> questionTexts = new ArrayList<>();
        ArrayList<ArrayList<String>> answers = new ArrayList<>();
        ArrayList<String> correctAnswers = new ArrayList<>();

        questionTexts.add("Which is not a member of the Microsoft Office Suite?");
        ArrayList<String> answers1 = new ArrayList<>();
        answers1.add("Excel");
        answers1.add("Outlook");
        answers1.add("Teams");
        answers1.add("Slides");
        answers.add(answers1);
        correctAnswers.add("Slides");

        questionTexts.add("In what year did George Washington first take office as president of the United States?");
        ArrayList<String> answers2 = new ArrayList<>();
        answers2.add("1776");
        answers2.add("1783");
        answers2.add("1789");
        answers2.add("1800");
        answers.add(answers2);
        correctAnswers.add("1789");

        questionTexts.add("What does the term \"icosahedron\" refer to?");
        ArrayList<String> answers3 = new ArrayList<>();
        answers3.add("a 24-sided polyhedron");
        answers3.add("a 20-sided polyhedron");
        answers3.add("a 15-sided polyhedron");
        answers3.add("a 12-sided polyhedron");
        correctAnswers.add("a 20-sided polyhedron");

        questions = new ArrayList<>();

        for (int i = 0; i < numQuestions; i++) {
            Question question = new Question(questionTexts.get(i), answers.get(i), correctAnswers.get(i));
            questions.add(question);
        }
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    final public class Question {

        String questionText;
        ArrayList<String> answers;
        String correctAnswer;

        Question(String questionText, ArrayList<String> answers, String correctAnswer) {
            this.questionText = questionText;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public ArrayList<String> getAnswers() {
            return answers;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
