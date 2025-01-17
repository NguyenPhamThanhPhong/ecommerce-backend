package ecommerce.api.controller;

import ecommerce.api.dto.DataChangeResponse;
import ecommerce.api.dto.account.request.*;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.service.business.AccountService;
import ecommerce.api.service.smtp.SMTPService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
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
    private final SMTPService smtpService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAccount(
            @ModelAttribute @Valid AccountCreateRequest request) throws IOException {
        DataChangeResponse account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @PostMapping(value = "/password-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OTPRequest request) throws MessagingException {
        accountService.saveOtp(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordUpdateRequest request) {
        accountService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        DataChangeResponse account = accountService.register(request);
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
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id,
                                           @RequestParam(required = false, defaultValue = "true") boolean isSoft,
                                           Authentication authentication) {
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

    @PutMapping("")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountUpdateRequest request) throws IOException {
        DataChangeResponse dataChangeResponse = accountService.updateAccount(request);
        return ResponseEntity.ok(dataChangeResponse);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> deleteMyAccount(Authentication authentication, @RequestParam(required = false, defaultValue = "true") boolean isSoft) {
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
