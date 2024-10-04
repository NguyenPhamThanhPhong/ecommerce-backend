package ecommerce.api.controller;

import ecommerce.api.dto.account.AccountCreateRequest;
import ecommerce.api.dto.account.ProfileUpdateRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.service.business.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity<?> createAccount(
            @RequestBody AccountCreateRequest request) {
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
                                           @RequestBody ProfileUpdateRequest request) {
        request.setId(id);
        Profile profile = accountService.updateProfile(request);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
