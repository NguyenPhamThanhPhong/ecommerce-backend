package ecommerce.api.service.business;

import ecommerce.api.dto.coupon.request.CouponCreateRequest;
import ecommerce.api.dto.coupon.request.CouponUpdateRequest;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.product.Product;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.CouponMapper;
import ecommerce.api.repository.ICouponRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final ICouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final CloudinaryService cloudinaryService;


    public PaginationDTO<CouponResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Coupon> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Coupon> coupons = couponRepository.findAll(spec, pageable);
        Page<CouponResponse> responses = coupons.map(couponMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }
    
    
    public CouponResponse findById(UUID id) {
        Coupon c = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON NOT FOUND"));
        return couponMapper.fromEntityToResponse(c);
    }

    @Transactional
    public CouponResponse insert(CouponCreateRequest request) throws IOException {
        Coupon coupon = couponMapper.fromCreateRequestToEntity(request);
        if(request.getImage()!=null)
        {
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),cloudinaryService.PRODUCT_DIR,coupon.getId().toString());
            coupon.setImageUrl(imageUrl);
        }
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }

    @Transactional
    public CouponResponse update(CouponUpdateRequest request) throws IOException {
        Coupon coupon = couponMapper.fromUpdateRequestToEntity(request);
        coupon.setId(request.getId());
        if( request.getImage() != null)
        {
            cloudinaryService.deleteFile(coupon.getId().toString());
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),cloudinaryService.COUPON_DIR,coupon.getId().toString());
            coupon.setImageUrl(imageUrl);
        }
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }


    public int deleteCouponById(UUID id) {
        return couponRepository.deleteCouponById(id);
    }
    
}
