import com.google.api.client.json.Json;
import com.google.gson.Gson;
import domain.googlecloud.Transcript;
import domain.googlecloud.UploadedObject;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.examples.PrecomputedCosine;
import info.debatty.java.stringsimilarity.experimental.Sift4;
import services.GoogleCloudAudioRecognition;
import services.GoogleCloudStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by ericreis on 10/7/17.
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    private static final String googleCloudBucketName = "speech2text-comparator-resources";
    private static final String resourceName = "leitura-em-nossa-vida";

    public static void main(String[] args) throws Exception {
        URL resourceUrl = Application.class.getClassLoader().getResource("audio/" + resourceName + ".wav");
        URL resourcesFolderUrl = Application.class.getClassLoader().getResource(".");

        if (resourceUrl != null) {
//            logger.info("Got resource: " + resourceUrl.getFile());
//
//            GoogleCloudStorageService googleCloudStorageService = new GoogleCloudStorageService();
//            UploadedObject uploadedObject = googleCloudStorageService.uploadFile(resourceUrl,
//                    Application.googleCloudBucketName);
//
//            logger.info("Uploaded object: " + uploadedObject);
//
//            GoogleCloudAudioRecognition googleCloudAudioRecognition = new GoogleCloudAudioRecognition();
//            Transcript transcript = googleCloudAudioRecognition.asyncRecognizeGcs(uploadedObject.getGcsUri(),
//                    "pt-BR");
//
//            transcript.dumpToFile(resourcesFolderUrl.getPath() + "transcript/" + resourceName + ".json");

            Path path = Paths.get(resourcesFolderUrl.getFile() + "/transcript/leitura-em-nossa-vida-original.txt");
            String originalText = new String(Files.readAllBytes(path), "UTF-8")
                    .replaceAll("[\\p{Punct}]", "")
                    .toLowerCase();

            Path jsonPath = Paths.get(resourcesFolderUrl.getFile() + "transcript/leitura-em-nossa-vida.json");
            String jsonString = new String(Files.readAllBytes(jsonPath), "UTF-8");
            Gson gson = new Gson();
            Transcript transcript = gson.fromJson(jsonString, Transcript.class);

            Levenshtein l = new Levenshtein();
            System.out.println("Levenshtein distance = " + l.distance(originalText, transcript.getText()));

            NGram ngram = new NGram(5);
            System.out.println("NGram distance = " + ngram.distance(originalText, transcript.getText()));

            Cosine cosine = new Cosine(5);
            Map<String, Integer> profile1 = cosine.getProfile(originalText);
            Map<String, Integer> profile2 = cosine.getProfile(transcript.getText());
            System.out.println("Cosine similarity = " + cosine.similarity(profile1, profile2));
        }
    }
}
