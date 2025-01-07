package ecommerce.api.service.business;

import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductImageRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
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
import java.text.SimpleDateFormat;
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
//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        Date value = isoFormat.parse((String) "2025-01-04T17:20:38.403Z");
//        Specification<Product> spec = (root, query, criteriaBuilder) -> {
//            return criteriaBuilder.greaterThanOrEqualTo(root.get("availableDate"), new Date());
//        };
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
    public ProductResponse insert(ProductCreateRequest request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);
        handleProductImages(product, request.getProductImages());
        checkAndAddThumbNail(product, request.getThumbnail());
        return productMapper.fromEntityToResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(ProductUpdateRequest request) throws IOException {
        Product product = productMapper.fromUpdateRequestToEntity(request);
        checkAndAddThumbNail(product, request.getThumbnail());
        if (request.getRemovalImageIds() != null && !request.getRemovalImageIds().isEmpty()) {
            String publicId = product.getId().toString() + "_";
            for (Integer seqNo : request.getRemovalImageIds()) {
                cloudinaryService.deleteFile(publicId + seqNo);
            }
            productImageRepository.deleteAllByProductIdAndSeqNoIn(product.getId(), request.getRemovalImageIds());
        }
        handleProductImages(product, request.getAppendingImages());
        return productMapper.fromEntityToResponse(productRepository.save(product));
    }

    public int addFavorite(UUID accountId, UUID productId) {
        return productRepository.insertFavoriteProduct(accountId, productId);
    }

    public int removeFavorite(UUID accountId, UUID productId) {
        return productRepository.deleteFavoriteProduct(accountId, productId);
    }

    public PaginationDTO<ProductResponse> findFavorites(UUID accountId, Pageable pageable) {
        Page<Product> products = productRepository.findFavorites(accountId, pageable);
        Page<ProductResponse> responses = products.map(productMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    private void handleProductImages(Product product, List<ProductImageRequest> appendingImages) throws IOException {
        if (appendingImages != null && !appendingImages.isEmpty()) {
            List<ProductImage> productImages = new ArrayList<>();
            for (ProductImageRequest request : appendingImages) {
                ProductImage productImage = handleProductImage(request, product.getId());
                productImages.add(productImage);
            }
            product.setProductImages(productImages);
        }
    }

    private ProductImage handleProductImage(ProductImageRequest request, UUID productId) throws IOException {
        if (request == null || request.getImage() == null)
            throw new BadRequestException("Product image is required");
        ProductImage productImage = productMapper.fromImageRequestToImage(request);
        productImage.setProductId(productId);
        String publicId = productId.toString() + "_" + request.getSeqNo();
        String imageUrl = cloudinaryService.uploadFile(request.getImage(), cloudinaryProperties.getProductDir(), publicId);
        productImage.setImageUrl(imageUrl);
        return productImage;
    }

    private void checkAndAddThumbNail(Product product, MultipartFile file) throws IOException {
        if (file != null) {
            String publicId = product.getId().toString();
            String imageUrl = cloudinaryService.uploadFile(file, cloudinaryProperties.getProductDir(), publicId);
            product.setThumbnailUrl(imageUrl);
        }
    }

    public int deleteProductById(UUID id) {
        return productRepository.deleteProductById(id);
    }
}
