package ecommerce.api.service.business;

import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.account.request.ProfileUpdateRequest;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.AccountMapper;
import ecommerce.api.repository.IAccountRepository;
import ecommerce.api.service.azure.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CloudinaryService blobService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) throws IOException {
        Account account = accountMapper.fromCreateRequestToEntity(request);

        MultipartFile file = request.getProfile().getAvatar();
        if (file != null) {
            String blobUrl = blobService.uploadFile(file, cloudinaryService.ACCOUNT_DIR, account.getId().toString());
            account.getProfile().setAvatarUrl(blobUrl);
        }
        return accountMapper.fromEntityToAccountResponse(accountRepository.save(account));
    }

    public AccountResponse getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return accountMapper.fromEntityToAccountResponse(account);
    }

    public PaginationDTO<AccountResponse> getAccount(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        Page<AccountResponse> accountResponses = accounts.map(accountMapper::fromEntityToAccountResponse);
        return PaginationDTO.fromPage(accountResponses);
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileUpdateRequest request) throws IOException {
        Profile profile = accountMapper.fromUpdateRequestToEntity(request);
        if (request.getAvatar() != null) {
            String blobUrl = blobService.uploadFile(request.getAvatar(),
                    cloudinaryService.ACCOUNT_DIR, request.getId().toString());
            profile.setAvatarUrl(blobUrl);
        }
        accountRepository.updateProfile(profile);
        return accountMapper.fromEntityToProfileResponse(profile);
    }

    @Transactional
    public int deleteAccount(UUID id, boolean isSoft) {
        if (isSoft) {
            return accountRepository.updateAccountDeletedAt(id);
        }
        return accountRepository.deleteAccountById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailOrLoginId(username);
        return accountMapper.fromEntityToUserDetailDTO(account);
    }
}
