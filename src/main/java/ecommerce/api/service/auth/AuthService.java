package ecommerce.api.service.auth;

import ecommerce.api.dto.account.request.LoginRequest;
import ecommerce.api.dto.account.response.LoginResponse;
import ecommerce.api.entity.user.Account;
import ecommerce.api.exception.UnAuthorisedException;
import ecommerce.api.repository.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IAccountRepository accountService;
    private final JwtService jwtService;

    public LoginResponse authenticateAccount(LoginRequest request) {
        Account account = accountService.findByEmailOrLoginId(request.getUsername());
        if (account != null && account.getPassword().equals(request.getPassword())) {
            return LoginResponse.builder()
                    .accessToken(jwtService.generateAccessToken(account))
                    .refreshToken(jwtService.generateRefreshToken(account.getLoginId())).build();
        }
        throw new UnAuthorisedException("Invalid username or password");
    }

}
