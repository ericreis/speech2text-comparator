package domain.googlecloud;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ericreis on 10/7/17.
 */
public class Transcript {

    private String id;
    private String text;
    private float confidence;
    private Evaluation evaluation;

    private int charsCount;

    public Transcript(String id) {
        this.id = id;
        this.text = "";
        this.confidence = .0f;
    }

    public String getText() {
        return this.text;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public void update(SpeechRecognitionAlternative alternative) {
        this.text += alternative.getTranscript();
        this.charsCount += alternative.getTranscript().length();
        this.confidence += this.charsCount == 0 ? alternative.getConfidence() :
                (alternative.getConfidence() * alternative.getTranscript().length()) / this.charsCount;
    }

    public void dumpToFile(String fileUrl) throws IOException {
        FileUtils.writeStringToFile(new File(fileUrl), this.asJsonString());
    }

    public String asJsonString() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return super.toString() + " = " + this.asJsonString();
    }
}
