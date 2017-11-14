package domain.googlecloud;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
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
    
    private int wordsCount;

    public Transcript(String id) {
        this.id = id;
        this.text = "";
        this.confidence = .0f;
    }

    public void update(SpeechRecognitionAlternative alternative) {
        this.text += alternative.getTranscript();
        this.wordsCount += alternative.getWordsCount();
        this.confidence += this.wordsCount == 0 ? alternative.getConfidence() :
                (alternative.getConfidence() * alternative.getWordsCount()) / this.wordsCount;
    }

    public void dumpToFile(String fileUrl) throws IOException {
        FileUtils.writeStringToFile(new File(fileUrl), this.asJsonString());
    }

    public String asJsonString() {
        return String.format("{ \n\t \"id\": \"%s\", \n\t \"text\": \"%s\", \n\t \"confidence\": %f \n }",
                this.id, this.text, this.confidence);
    }

    @Override
    public String toString() {
        return super.toString() + " = " + this.asJsonString();
    }
}
