package Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private List<String> answers;

    @JsonProperty("correct_ans")
    private String correctAns;

    @JsonProperty("difficulty")
    private String difficulty;

    public Question() {
        // Default constructor for Jackson
    }

    public Question(String question, List<String> answers, String correctAns, String difficulty) {
        this.question = question;
        this.answers = answers;
        this.correctAns = correctAns;
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                ", correctAns='" + correctAns + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
