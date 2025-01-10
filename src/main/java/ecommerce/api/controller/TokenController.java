package ecommerce.api.controller;

import ecommerce.api.dto.account.request.LoginRequest;
import ecommerce.api.dto.account.response.LoginResponse;
import ecommerce.api.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;
    @PostMapping("")
    public ResponseEntity<?> getTokens(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticateAccount(loginRequest);
        Cookie[] cookies = authService.makeCookies(loginResponse);
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteTokens(HttpServletResponse response) {
        Cookie[] cookies = authService.removeAuthCookies();
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("phong")
    public ResponseEntity<?> getPhong() {
        return ResponseEntity.ok("Phong");
    }

    @GetMapping("doom/{status}")
    public ResponseEntity<?> getDoom(@PathVariable int status) {
        return ResponseEntity.status(status).build();
    }
}
