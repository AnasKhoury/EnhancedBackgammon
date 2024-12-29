package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SysData {

    // Singleton instance
    private static volatile SysData instance;

    // Data fields
    private final List<Question> questions; // List of all questions
    private final List<Game> gameHistories; // History of past games

    // Private constructor
    private SysData() {
        this.questions = new ArrayList<>();
        this.gameHistories = new ArrayList<>();
    }

    // Get Singleton instance
    public static SysData getInstance() {
        if (instance == null) {
            synchronized (SysData.class) {
                if (instance == null) {
                    instance = new SysData();
                }
            }
        }
        return instance;
    }

    // Add a question
    public boolean addQuestion(Question question) {
        if (question == null || questions.stream().anyMatch(q -> q.getQuestion().equalsIgnoreCase(question.getQuestion()))) {
            return false; // Reject null or duplicate questions
        }
        questions.add(question);
        return true;
    }

    // Add multiple questions
    public void addQuestions(List<Question> newQuestions) {
        if (newQuestions != null) {
            newQuestions.forEach(this::addQuestion); // Add only valid and non-duplicate questions
        }
    }

    // Edit a question
    public boolean editQuestion(String oldQuestionText, Question updatedQuestion) {
        if (oldQuestionText == null || updatedQuestion == null) {
            return false; // Reject null inputs
        }
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestion().equalsIgnoreCase(oldQuestionText)) {
                questions.set(i, updatedQuestion);
                return true;
            }
        }
        return false; // Question not found
    }

    // Delete a question
    public boolean deleteQuestion(String questionText) {
        if (questionText == null) {
            return false; // Reject null input
        }
        return questions.removeIf(q -> q.getQuestion().equalsIgnoreCase(questionText));
    }

    // Get all questions (immutable view)
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    // Add a game history record
    public boolean addGameHistory(Game history) {
        if (history == null) {
            return false; // Reject null input
        }
        gameHistories.add(history);
        return true;
    }

    // Get all game histories (immutable view)
    public List<Game> getGameHistories() {
        return Collections.unmodifiableList(gameHistories);
    }

    // Clear all questions
    public void clearQuestions() {
        questions.clear();
    }

    // Clear all game histories
    public void clearGameHistories() {
        gameHistories.clear();
    }

    @Override
    public String toString() {
        return "SysData{" +
                "questions=" + questions +
                ", gameHistories=" + gameHistories +
                '}';
    }
}
