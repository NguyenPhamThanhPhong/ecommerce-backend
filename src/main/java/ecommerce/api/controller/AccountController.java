package ecommerce.api.controller;

import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.dto.account.request.LoginRequest;
import ecommerce.api.dto.account.request.ProfileUpdateRequest;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.LoginResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.service.auth.AuthService;
import ecommerce.api.service.business.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
@Tag(name = "Account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAccount(
            @ModelAttribute @Valid AccountCreateRequest request) throws IOException {
        AccountResponse account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Integer code) {
        AccountResponse account = accountService.getByCode(code);
        return ResponseEntity.ok(account);
    }

    // query & get infos
    @PostMapping("searches")
    public ResponseEntity<?> search(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<AccountResponse> accounts = accountService.search(searchSpecs, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute ProfileUpdateRequest request) throws IOException {
        ProfileResponse profile = accountService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id, @RequestParam boolean isSoft, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        if (auth.getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }
        return accountService.deleteAccount(id, isSoft) == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        AccountResponse accountResponse = accountService.getByCode(userDetailDTO.getCode());
        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> deleteMyAccount(Authentication authentication, @RequestParam boolean isSoft) {
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        return accountService.deleteAccount(userDetailDTO.getId(), isSoft) == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/addresses")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> updateAddress(Authentication authentication, @RequestParam(required = false) String defaultAddress,
                                           @RequestBody Map<String, String> request) {
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        int changes = accountService.updateAddresses(userDetailDTO.getId(), request, defaultAddress);
        return changes == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
