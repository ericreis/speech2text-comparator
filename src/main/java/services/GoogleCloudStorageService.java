package services;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import domain.googlecloud.UploadedObject;
import infrastructure.factory.GoogleCloudStorageFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Created by ericreis on 10/7/17.
 */
public class GoogleCloudStorageService {

    /**
     * Uploads data to an object in a bucket.
     *
     * @param fileUrl the URL of the file to upload.
     * @param bucketName the name of the bucket to create the object in.
     */
    public UploadedObject uploadFile(URL fileUrl, String bucketName) throws IOException, GeneralSecurityException {
        URLConnection fileUrlConnection = fileUrl.openConnection();
        InputStreamContent contentStream = new InputStreamContent(fileUrlConnection.getContentType(), fileUrl.openStream());
        // Setting the length improves upload performance
        contentStream.setLength(fileUrlConnection.getContentLengthLong());
        StorageObject objectMetadata = new StorageObject()
                // Set the destination object name
                .setName(FilenameUtils.getName(fileUrl.getFile()))
                // Set the access control list to publicly read-only
                .setAcl(Arrays.asList(new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        // Do the insert
        Storage client = GoogleCloudStorageFactory.getService();
        Storage.Objects.Insert insertRequest = client.objects().insert(bucketName, objectMetadata, contentStream);

        StorageObject insertedObject = insertRequest.execute();

        return new UploadedObject(insertedObject);
    }

}
