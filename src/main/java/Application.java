import domain.googlecloud.Transcript;
import domain.googlecloud.UploadedObject;
import services.GoogleCloudAudioRecognition;
import services.GoogleCloudStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

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
            logger.info("Got resource: " + resourceUrl.getFile());

            GoogleCloudStorageService googleCloudStorageService = new GoogleCloudStorageService();
            UploadedObject uploadedObject = googleCloudStorageService.uploadFile(resourceUrl,
                    Application.googleCloudBucketName);

            logger.info("Uploaded object: " + uploadedObject);

            GoogleCloudAudioRecognition googleCloudAudioRecognition = new GoogleCloudAudioRecognition();
            Transcript transcript = googleCloudAudioRecognition.asyncRecognizeGcs(uploadedObject.getGcsUri(),
                    "pt-BR");

            transcript.dumpToFile(resourcesFolderUrl.getPath() + "transcript/" + resourceName + ".json");
        }
    }
}
