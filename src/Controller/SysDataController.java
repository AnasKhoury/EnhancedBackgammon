package Controller;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.QuestionsWrapper;
import Model.SysData;

public class SysDataController {
    private final String jsonFilePath;

    public SysDataController() {
        this.jsonFilePath = "src/question.json";
    }

    public boolean loadQuestionsFromJson() {
        synchronized (SysData.getInstance()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                QuestionsWrapper wrapper = objectMapper.readValue(new File(jsonFilePath), QuestionsWrapper.class);
                SysData.getInstance().clearQuestions();
                SysData.getInstance().addQuestions(wrapper.getQuestions());
                System.out.println("Questions loaded successfully.");
                return true;
            } catch (IOException e) {
                System.out.println("Error loading questions: " + e.getMessage());
                return false;
            }
        }
    }

    public boolean saveQuestionsToJson() {
        synchronized (SysData.getInstance()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File(jsonFilePath), new QuestionsWrapper(SysData.getInstance().getQuestions()));
                System.out.println("Questions saved successfully.");
                return true;
            } catch (IOException e) {
                System.out.println("Error saving questions: " + e.getMessage());
                return false;
            }
        }
    }
}
