package ecommerce.api.service.business;

import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductChangesResponse;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.product.ProductImage;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.ProductMapper;
import ecommerce.api.repository.IProductImageRepository;
import ecommerce.api.repository.IProductRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;

    @SneakyThrows
    public PaginationDTO<ProductResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Product> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Product> products = productRepository.findAll(spec, pageable);
        Page<ProductResponse> responses = products.map(productMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    public ProductResponse findByCode(Integer code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUCT NOT FOUND"));
        return productMapper.fromEntityToResponse(product);
    }

    public PaginationDTO<ProductResponse> findInCodesOrIds(List<Integer> codes, List<UUID> ids, Pageable pageable) {
        Page<Product> products = productRepository.findAllByCodeInOrIdIn(codes, ids, pageable);
        Page<ProductResponse> responses = products.map(productMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    @Transactional
    public ProductChangesResponse insert(ProductCreateRequest request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);
        checkAndAddThumbNail(product, request.getThumbnail());
        List<ProductImage> productImages = saveImages(product.getId(), request.getProductImages());
        productRepository.insert(product);
        product.setProductImages(productImages);
        return productMapper.fromEntityToChangesResponse(product);
    }

    @Transactional
    public ProductChangesResponse update(ProductUpdateRequest request) throws IOException {
        Product product = productMapper.fromUpdateRequestToEntity(request);
        checkAndAddThumbNail(product, request.getThumbnail());
        removeImages(product.getId(), request.getRemovalImageIds());
        List<ProductImage> productImages = saveImages(product.getId(), request.getAppendingImages());
        productRepository.update(product);
        return productMapper.fromEntityToChangesResponse(product);
    }

    public int deleteProductById(UUID id) {
        return productRepository.updateProductDeletedAt(id);
    }

    public int addFavorite(UUID accountId, UUID productId) {
        try{
            return productRepository.insertFavoriteProduct(accountId, productId);
        }
        catch (Exception e){
            return 0;
        }
    }

    public int removeFavorite(UUID accountId, UUID productId) {
        return productRepository.deleteFavoriteProduct(accountId, productId);
    }

    public PaginationDTO<ProductResponse> findFavorites(UUID accountId, Pageable pageable) {
        Page<Product> products = productRepository.findFavorites(accountId, pageable);
        Page<ProductResponse> responses = products.map(productMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    private void removeImages(UUID productId, List<Integer> removalImageIds) throws IOException {
        if (removalImageIds != null && !removalImageIds.isEmpty()) {
            String publicId = productId.toString() + "_";
            for (Integer seqNo : removalImageIds) {
                cloudinaryService.deleteFile(publicId + seqNo);
            }
            productImageRepository.deleteAllByProductIdAndSeqNoIn(productId, removalImageIds);
        }
    }

    private List<ProductImage> saveImages(UUID productId, List<MultipartFile> imageFiles) throws IOException {
        List<ProductImage> productImages = new ArrayList<>(imageFiles.size());
        if (!imageFiles.isEmpty()) {
            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                ProductImage productImage = productMapper.fromMultipartFileToProductImage(i, file);
                productImage.setProductId(productId);
                String url = saveSingleImageToCloud(productImage, file);
//                String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScWo-hawnOFdwHxQcPk8k5a7Kyk94pps-fYw&s";
                productImage.setImageUrl(url);
                productImages.add(productImage);
            }
        }
        return productImageRepository.saveAll(productImages);
    }

    private String saveSingleImageToCloud(ProductImage productImage, MultipartFile file) throws IOException {
        if (productImage == null)
            throw new BadRequestException("Product image is required");
        String publicId = productImage.getProductId().toString() + "_" + productImage.getSeqNo();
        String imageUrl = cloudinaryService.uploadFile(file, cloudinaryProperties.getProductDir(), publicId);
        productImage.setImageUrl(imageUrl);
        return imageUrl;
    }

    private void checkAndAddThumbNail(Product product, MultipartFile file) throws IOException {
        if (file != null) {
            String publicId = product.getId().toString();
            String imageUrl = cloudinaryService.uploadFile(file, cloudinaryProperties.getProductDir(), publicId);
            product.setThumbnailUrl(imageUrl);
        }
    }


}
