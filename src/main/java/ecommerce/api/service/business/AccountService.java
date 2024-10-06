package ecommerce.api.service.business;

import ecommerce.api.dto.account.AccountCreateRequest;
import ecommerce.api.dto.account.ProfileResponse;
import ecommerce.api.dto.account.ProfileUpdateRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.mapper.AccountMapper;
import ecommerce.api.repository.IAccountRepository;
import lombok.RequiredArgsConstructor;
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
    private final AzureBlobService blobService;

    public Account createAccount(AccountCreateRequest request) throws IOException {
        Account account = accountMapper.fromCreateRequestToEntity(request);
        MultipartFile file = request.getProfile().getAvatar();
        String blobName = "avatar-" + account.getId();
        String blobUrl = blobService.uploadBlob(blobName, file.getInputStream(), file.getSize(), true);
        account.getProfile().setAvatarUrl(blobUrl);
        return accountRepository.save(account);
    }

    public Account getAccount(UUID id) {
        return accountRepository.findById(id).orElse(null);
    }

    public ProfileResponse updateProfile(ProfileUpdateRequest request) throws IOException {
        Profile profile = accountMapper.fromUpdateRequestToEntity(request);
        if(request.getAvatar() != null) {
            String blobName = "avatar-" + request.getId();
            String blobUrl = blobService.uploadBlob(blobName, request.getAvatar().getInputStream(), request.getAvatar().getSize(), true);
            profile.setAvatarUrl(blobUrl);
        }
        accountRepository.updateProfile(profile);
        return accountMapper.fromEntityToProfileResponse(profile);
    }

    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailOrLoginId(String.valueOf(username), String.valueOf(username));
        return accountMapper.fromEntityToUserDetailDTO(account);
    }
}
