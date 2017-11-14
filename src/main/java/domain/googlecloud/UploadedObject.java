package domain.googlecloud;

import com.google.api.services.storage.model.StorageObject;

/**
 * Created by ericreis on 10/7/17.
 */
public class UploadedObject {

    private String bucket;
    private String object;

    public UploadedObject(StorageObject storageObject) {
        this.bucket = storageObject.getBucket();
        this.object = storageObject.getName();
    }

    public String getGcsUri() {
        return String.format("gs://%s/%s", this.bucket, this.object);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("[bucket=%s, object=%s]", this.bucket, this.object);
    }
}
