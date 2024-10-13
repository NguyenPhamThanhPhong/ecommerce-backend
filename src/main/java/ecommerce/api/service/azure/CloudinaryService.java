package ecommerce.api.service.azure;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@PropertySource("classpath:cloudinary.yml")
public class CloudinaryService {

    public final String PRODUCT_DIR;
    public final String ACCOUNT_DIR;
    public final String BLOG_DIR;
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloud-name}") String cloudName,
                             @Value("${api-key}") String apiKey,
                             @Value("${api-secret}") String apiSecret,
                             @Value("${product-folder:ecommerce/product}") String PRODUCT_DIR,
                             @Value("${account-folder:ecommerce/account}") String ACCOUNT_DIR,
                             @Value("${blog-folder:ecommerce/blog}") String BLOG_DIR) {
        cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey,
                "api_secret", apiSecret, "secure", true));
        this.PRODUCT_DIR = PRODUCT_DIR;
        this.ACCOUNT_DIR = ACCOUNT_DIR;
        this.BLOG_DIR = BLOG_DIR;
    }

    // Upload a file to a folder (including nested folders)
    public String uploadFile(MultipartFile file, String folderPath, String publicId) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderPath,
                        "public_id", publicId,
                        "overwrite", true)); // folderPath is the full path you want to upload to
        return uploadResult.get("secure_url").toString();  // Return the uploaded file's URL
    }

    // Delete a file by public ID
    public String deleteFile(String publicId) throws IOException {
        Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return deleteResult.get("result").toString(); // Return the result (ok / not found)
    }

    public String[] uploadMultipleFiles(MultipartFile[] files, String folderPath) throws IOException {
        // Create an array to store the CompletableFuture<String> objects
        @SuppressWarnings("unchecked")
        CompletableFuture<String>[] futures = new CompletableFuture[files.length];
        // Initialize the futures array by submitting each upload asynchronously
        for (int i = 0; i < files.length; i++) {
            final int index = i;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    return uploadFile(files[index], folderPath, UUID.randomUUID().toString()); // Asynchronously upload file
                } catch (IOException e) {
                    throw new RuntimeException(e); // Wrap the checked exception in a RuntimeException
                }
            });
        }
        return CompletableFuture.allOf(futures)
                .thenApply(v -> Arrays.stream(futures)
                        .map(future -> {
                            try {
                                return future.join();
                            } catch (Exception e) {
                                return null; // Return null if future failed
                            }
                        })
                        .toArray(String[]::new) // Convert to a String[] directly
                ).join(); // Wait for all futures to complete
    }

}
