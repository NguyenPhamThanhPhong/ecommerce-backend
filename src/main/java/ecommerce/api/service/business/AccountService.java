package ecommerce.api.service.business;

import ecommerce.api.dto.account.AccountCreateRequest;
import ecommerce.api.dto.account.ProfileUpdateRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import ecommerce.api.mapper.AccountMapper;
import ecommerce.api.repository.IAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    public Account createAccount(AccountCreateRequest request) {
        Account account = accountMapper.fromCreateRequestToEntity(request);
        accountRepository.save(account);
        return null;
    }
    public Account getAccount(UUID id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Profile updateProfile(ProfileUpdateRequest request){
        Profile profile = accountMapper.fromUpdateRequestToEntity(request);
        accountRepository.updateProfile(request.getId(), request.getFullName(), request.getDateOfBirth(), request.getPhone());
        return profile;
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
