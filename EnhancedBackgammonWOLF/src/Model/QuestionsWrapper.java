package Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionsWrapper {
	@JsonProperty("questions")
    private List<Question> questions;

    public QuestionsWrapper() {
    }

    public QuestionsWrapper(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
