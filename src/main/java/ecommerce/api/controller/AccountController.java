package ecommerce.api.controller;

import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.dto.account.request.LoginRequest;
import ecommerce.api.dto.account.request.ProfileUpdateRequest;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.entity.user.Account;
import ecommerce.api.mapper.AccountMapper;
import ecommerce.api.repository.IAccountRepository;
import ecommerce.api.service.auth.AuthService;
import ecommerce.api.service.business.AccountService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AuthService authService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAccount(
            @ModelAttribute @Valid AccountCreateRequest request) throws IOException {
        AccountResponse account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        AccountResponse account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    // query & get infos
    @PostMapping("filtered-paginated-info")
    public ResponseEntity<?> createTempAccount(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<AccountResponse> accounts = accountService.search(searchSpecs, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/tokens")
    public ResponseEntity<?> getTokens(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateAccount(loginRequest));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateProfile(@ModelAttribute ProfileUpdateRequest request) throws IOException {
        ProfileResponse profile = accountService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id, @RequestParam boolean isSoft) {
        return accountService.deleteAccount(id, isSoft) == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        return ResponseEntity.ok(userDetailDTO);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteMyAccount(Authentication authentication, @RequestParam boolean isSoft) {
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        return accountService.deleteAccount(userDetailDTO.getId(), isSoft) == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
