package ecommerce.api.controller;

import ecommerce.api.dto.coupon.request.CouponCreateRequest;
import ecommerce.api.dto.coupon.request.CouponUpdateRequest;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.service.business.CouponService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/filtered-paginated-info")
    public ResponseEntity<?> getAllCoupons(
            @ParameterObject Pageable pageable,
            @RequestBody(required = false) Set<SearchSpecification> specs) {
        PaginationDTO<CouponResponse> res = couponService.search(specs, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.findById(id));
    }

    @PostMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCoupon(@ModelAttribute CouponCreateRequest request) throws IOException {
        return ResponseEntity.ok(couponService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCouponById(@PathVariable UUID id) {
        return ResponseEntity.ok(couponService.deleteCouponById(id));
    }

    @PutMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCoupon(@ModelAttribute CouponUpdateRequest request) throws IOException {
        return ResponseEntity.ok(couponService.update(request));
    }
}
