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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@PropertySource("classpath:cloudinary.yml")
public class CloudinaryService {

    public final String PRODUCT_DIR;
    public final String ACCOUNT_DIR;
    public final String BLOG_DIR;
    public final String CATEGORY_DIR;
    public final String BRAND_DIR;
    public final String COUPON_DIR;
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloud-name}") String cloudName,
                             @Value("${api-key}") String apiKey,
                             @Value("${api-secret}") String apiSecret,
                             @Value("${product-folder:ecommerce/product}") String PRODUCT_DIR,
                             @Value("${account-folder:ecommerce/account}") String ACCOUNT_DIR,
                             @Value("${category-folder:ecommerce/category}") String CATEGORY_DIR,
                             @Value("${brand-folder:ecommerce/brand}") String BRAND_DIR,
                             @Value("${blog-folder:ecommerce/blog}") String BLOG_DIR, @Value("${blog-folder:ecommerce/coupon") String COUPON_DIR) {
        cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey,
                "api_secret", apiSecret, "secure", true));
        this.PRODUCT_DIR = PRODUCT_DIR;
        this.ACCOUNT_DIR = ACCOUNT_DIR;
        this.CATEGORY_DIR = CATEGORY_DIR;
        this.BRAND_DIR = BRAND_DIR;
        this.BLOG_DIR = BLOG_DIR;
        this.COUPON_DIR = COUPON_DIR;
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
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch latch = new CountDownLatch(files.length);
        String[] results = new String[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        results[index] = uploadFile(files[index], folderPath, UUID.randomUUID().toString());
                    } catch (IOException e) {
                        results[index] = null; // Handle the exception by setting the result to null
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await(); // Wait for all tasks to complete
            return results;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("File upload interrupted", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
