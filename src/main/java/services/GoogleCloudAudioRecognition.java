package services;

import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.longrunning.Operation;
import domain.googlecloud.Transcript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by ericreis on 10/7/17.
 */
public class GoogleCloudAudioRecognition {

    private static final Logger logger = LogManager.getLogger(GoogleCloudAudioRecognition.class);

    /**
     * Performs non-blocking speech recognition on remote WAV file and prints
     * the transcription.
     *
     * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
     */
    public Transcript asyncRecognizeGcs(String gcsUri, String language) throws Exception {
        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        SpeechClient speech = SpeechClient.create();

        // Configure remote file request for Linear16
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setLanguageCode(language)
//                .setSampleRateHertz(16000)
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setUri(gcsUri)
                .build();

        // Use non-blocking call for getting file transcription
        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata, Operation> response = speech
                .longRunningRecognizeAsync(config, audio);
        while (!response.isDone()) {
            System.out.println("Waiting for response...");
            Thread.sleep(10000);
        }

        List<SpeechRecognitionResult> results = response.get().getResultsList();

        String[] splitedUri = gcsUri.split("/");
        Transcript transcript = new Transcript(splitedUri[splitedUri.length - 1]);

        for (SpeechRecognitionResult result: results) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            logger.info("Transcription: %s\n", alternative.getTranscript());
            transcript.update(alternative);
        }
        speech.close();

        return transcript;
    }

}
