package ecommerce.api.controller;

import ecommerce.api.dto.account.AccountCreateRequest;
import ecommerce.api.dto.account.ProfileCreateRequest;
import ecommerce.api.dto.account.ProfileResponse;
import ecommerce.api.dto.account.ProfileUpdateRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.service.business.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<?> createAccount(
            @ModelAttribute AccountCreateRequest request) throws IOException {
        Account account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable UUID id) {
        Account account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable UUID id,
                                           @ModelAttribute ProfileUpdateRequest request) throws IOException {
        request.setId(id);
        ProfileResponse profile = accountService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
