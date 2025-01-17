package ecommerce.api.controller;

import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.service.business.StatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/me/orders")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> getStatistic(Authentication authentication) {
        UserDetailDTO userDetail = (UserDetailDTO) authentication.getPrincipal();
        return ResponseEntity.ok(statisticService.getSelfStatistics(userDetail.getId()));
    }
    @GetMapping("")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(statisticService.getDashboardStatistics());
    }


    
}
