package ecommerce.api.service.business;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@PropertySource("classpath:azure.yml")
public class AzureBlobService {
    private final BlobContainerClient blobContainerClient;

    @Autowired
    public AzureBlobService(@Value("${sass-url}") String sasURL,@Value("${container-name}") String containerName) {
        blobContainerClient = new BlobContainerClientBuilder()
                .sasToken(sasURL)
                .buildClient();
        blobContainerClient.getBlobClient(containerName);
    }

    public String uploadBlob(String blobName, InputStream data, long fileLength,boolean overwrite) {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        // Upload the blob
        blobClient.upload(data, fileLength, true); // 'true' for overwriting if blob exists
        // Return the URL of the uploaded blob
        return blobClient.getBlobUrl();
    }
    public Map<String, String> uploadMultipleBlobs(Map<String, InputStream> blobs) throws BadRequestException {
        Map<String, String> blobUrls = new HashMap<>();
        blobs.forEach((blobName, data) -> {
            try {
                BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
                blobClient.upload(data, data.available(), true);
                // Store the blob URL in the map
                blobUrls.put(blobName, blobClient.getBlobUrl());
            } catch (IOException ignored) {
            }
        });
        Set<String> failedSet = blobs.keySet();
        failedSet.removeAll(blobUrls.keySet());
        if (!failedSet.isEmpty()) {
            throw new BadRequestException("Error uploading blobs: " + failedSet);
        }
        return blobUrls;
    }

    public void deleteBlob(String blobName) {
        blobContainerClient.getBlobClient(blobName).delete();
    }

    public void deleteMultipleBlobs(Set<String> blobNames) {
        blobNames.forEach(blobContainerClient::getBlobClient);
    }

}
