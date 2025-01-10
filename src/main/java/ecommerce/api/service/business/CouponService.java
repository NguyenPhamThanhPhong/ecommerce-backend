package ecommerce.api.service.business;

import ecommerce.api.constants.CouponType;
import ecommerce.api.dto.coupon.request.CouponCreateRequest;
import ecommerce.api.dto.coupon.request.CouponUpdateRequest;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.CouponMapper;
import ecommerce.api.repository.ICouponRepository;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final ICouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public PaginationDTO<CouponResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Coupon> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Coupon> coupons = couponRepository.findAll(spec, pageable);
        Page<CouponResponse> responses = coupons.map(couponMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    public Pair<Coupon,BigDecimal> evaluateDiscount(BigDecimal amount, String code) {
        Coupon coupon = couponRepository.updateCouponUsage(code)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON NOT FOUND OR EXPIRED"));
        if (coupon.getCouponType().equals(CouponType.PERCENT)) {
            BigDecimal discount = amount.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
            amount = amount.subtract(discount);
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return new Pair<>(coupon, BigDecimal.ZERO);
            }
            return new Pair<>(coupon,amount);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return new Pair<>(coupon, BigDecimal.ZERO);
        }
        return new Pair<>(coupon, amount.subtract(coupon.getValue()));
    }
    
    
    public CouponResponse findByCode(String id) {
        Coupon c = couponRepository.findByCode(id)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON NOT FOUND"));
        return couponMapper.fromEntityToResponse(c);
    }

    @Transactional
    public CouponResponse insert(CouponCreateRequest request) throws IOException {
        Coupon coupon = couponMapper.fromCreateRequestToEntity(request);
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }

    @Transactional
    public CouponResponse update(CouponUpdateRequest request) throws IOException {
        Coupon coupon = couponMapper.fromUpdateRequestToEntity(request);
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }

    public int deleteCoupon(UUID id, boolean isSoft) {
        if (isSoft) {
            return couponRepository.updateDeletedAtById(id);
        }
        return couponRepository.deleteCouponById(id);
    }
    
}
