package ecommerce.api.service.business;

import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.DataChangeResponse;
import ecommerce.api.dto.account.request.*;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.exception.UnAuthorisedException;
import ecommerce.api.mapper.AccountMapper;
import ecommerce.api.repository.IAccountRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.service.smtp.SMTPService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;
    private final SMTPService smtpService;

    @Transactional
    public void saveOtp(OTPRequest otpRequest) throws MessagingException {
        int ran = new Random().nextInt(999999);
        String otp = String.format("%06d", ran);
        int changes = accountRepository.saveOtp(otpRequest.getEmail(), otp);
        smtpService.sendOTPEmail(otpRequest.getEmail(), otp);
        if (changes == 0) {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    @Transactional
    public void changePassword(PasswordUpdateRequest request) {
        int changes = accountRepository.changePassword(request.getOtp(), request.getPassword());
        if (changes == 0) {
            throw new ResourceNotFoundException("Invalid OTP or Account not found");
        }
    }

    @Transactional
    public DataChangeResponse createAccount(AccountCreateRequest request) throws IOException {
        Account account = accountMapper.fromCreateRequestToEntity(request);
        MultipartFile file = request.getProfile().getAvatar();
        if (file != null) {
            String blobUrl = cloudinaryService.uploadFile(file,
                    cloudinaryProperties.getAccountDir(), account.getId().toString());
            account.getProfile().setAvatarUrl(blobUrl);
            account.getProfile().setId(account.getId());
        }
        accountRepository.save(account);
        return new DataChangeResponse(account.getId(), account.getProfile().getAvatarUrl());
    }

    public PaginationDTO<AccountResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Account> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return PaginationDTO.fromPage(accounts.map(accountMapper::fromEntityToAccountResponse));
    }

    public AccountResponse getByCode(Integer code) {
        Account account = accountRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return accountMapper.fromEntityToAccountResponse(account);
    }

    @Transactional
    public DataChangeResponse register(RegistrationRequest request) {
        Account account = accountMapper.fromRegistrationRequestToEntity(request);
        accountRepository.register(account);
        account.getProfile().setId(account.getId());
        int insertProfileResult = accountRepository.registerProfile(account.getProfile());
        if (insertProfileResult == 0) {
            throw new BadRequestException("Profile not found");
        }
        return new DataChangeResponse(account.getId(), account.getProfile().getAvatarUrl());
    }

    @Transactional
    public DataChangeResponse updateAccount(AccountUpdateRequest request) throws IOException {
        Account account = accountMapper.fromAccountUpdateRequestToEntity(request);
        accountRepository.updateAccount(account);
        if (request.getProfile().getAvatar() != null) {
            String blobUrl = cloudinaryService.uploadFile(request.getProfile().getAvatar(),
                    cloudinaryProperties.getAccountDir(), request.getId().toString());
            Profile p = account.getProfile();
            p.setAvatarUrl(blobUrl);
            accountRepository.updateProfile(p);
            return new DataChangeResponse(request.getId(), p.getAvatarUrl());
        }
        Profile p = account.getProfile();
        accountRepository.updateProfileExcludeAvatar(p);
        return new DataChangeResponse(request.getId(), null);
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileUpdateRequest request) throws IOException {
        Profile profile = accountMapper.fromUpdateRequestToEntity(request);
        if (request.getAvatar() != null) {
            String blobUrl = cloudinaryService.uploadFile(request.getAvatar(),
                    cloudinaryProperties.getAccountDir(), request.getId().toString());
            profile.setAvatarUrl(blobUrl);
            accountRepository.updateProfile(profile);
            return accountMapper.fromEntityToProfileResponse(profile);
        }
        accountRepository.updateProfileExcludeAvatar(profile);
        return accountMapper.fromEntityToProfileResponse(profile);
    }

    public int updateAddresses(UUID id, Map<String, String> addresses, String primaryAddress) {
        return accountRepository.updateAddresses(id, addresses, primaryAddress);
    }

    @Transactional
    public int deleteAccount(UUID id, boolean isSoft) {
        if (isSoft) {
            return accountRepository.updateAccountDeletedAt(id);
        }
        return accountRepository.deleteAccountById(id);
    }

    public UserDetails findById(UUID username) throws UsernameNotFoundException {
        Account account = accountRepository.findById(username).orElseThrow(() ->
                new UnAuthorisedException("Account not found"));
        return accountMapper.fromEntityToUserDetailDTO(account);
    }


}
