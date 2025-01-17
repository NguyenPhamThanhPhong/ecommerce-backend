package ecommerce.api.repository;

import ecommerce.api.dto.account.request.RegistrationRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    @Modifying
    @Query(
            """
                            update Profile a set
                            a.fullName = :#{#profile.fullName},
                            a.dateOfBirth = :#{#profile.dateOfBirth},
                            a.phone = :#{#profile.phone},
                            a.avatarUrl = :#{#profile.avatarUrl}
                            where a.id = :#{#profile.id}
                    """
    )
    void updateProfile(@Param("profile") Profile profile);

    @Modifying
    @Query(
            """
                            update Profile a set
                            a.fullName = :#{#profile.fullName},
                            a.dateOfBirth = :#{#profile.dateOfBirth},
                            a.phone = :#{#profile.phone}
                            where a.id = :#{#profile.id}
                    """
    )
    void updateProfileExcludeAvatar(@Param("profile") Profile profile);


    @Query("""
                select a from Account a  where a.email = :data
            """)
    Account findByEmail(String data);

    @Modifying
    @Transactional
    int deleteAccountById(UUID id);

    @Modifying
    @Transactional
    @Query("update Account a set a.deletedAt = CURRENT_TIMESTAMP where a.id = :id")
    int updateAccountDeletedAt(UUID id);

    @Query("select a from Account a left join fetch a.profile")
    List<Account> findAllAccount(Specification<Account> spec);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    Optional<Account> findById(@NotNull UUID id);

    @Query(value = """
        select a.id, a.code, a.created_at, a.deleted_at, a.disable_date,
               a.email, a.enable_date, a.is_verified, a.password, a.role,
               a.otp, a.otp_expiry,
               p.id as profile_id, p.avatar_url, p.date_of_birth, p.full_name, p.phone
        from accounts a
        join profiles p on a.id = p.id
        where a.code = :code
        """, nativeQuery = true)
    Optional<Account> findByCode(@Param("code") long code);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    List<Account> findAll();

    @EntityGraph(attributePaths = {"profile"})
    Page<Account> findAll(@NotNull Specification<Account> spec, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Profile p set p.addresses = :addresses, p.primaryAddress = :primaryAddress where p.id = :id")
    int updateAddresses(UUID id, Map<String, String> addresses, String primaryAddress);

    @Modifying
    @Query(value = """
                insert into accounts (id,email, password,role,enable_date,disable_date,is_verified) 
                values (:#{#account.id}, :#{#account.email}, :#{#account.password}, 'ROLE_CUSTOMER',
                            '2000-01-03 04:10:48.640731','2040-01-03 04:10:48.640731',false)
            """, nativeQuery = true)
    void register(Account account);

    @Modifying
    @Query(value = """
                insert into profiles (id, full_name, phone, date_of_birth)
                values (:#{#profile.id}, :#{#profile.fullName}, :#{#profile.phone},
                            :#{#profile.dateOfBirth})
            """, nativeQuery = true)
    int registerProfile(Profile profile);


    @Modifying
    @Transactional
    @Query(value = """
                update accounts set email = :#{#account.email}, role = :#{#account.role}, 
                                    password = :#{#account.password}, 
                                    enable_date = :#{#account.enableDate}, disable_date = :#{#account.disableDate}, 
                                    is_verified = :#{#account.isVerified} where id = :#{#account.id}
            """, nativeQuery = true)
    void updateAccount(Account account);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    Optional<Account> findOne(Specification specification);

    @Modifying
    @Query(value = """
            update accounts set otp = :otp, otp_expiry = now() + interval '15 minutes' 
                            where email = :email
            """, nativeQuery = true)
    int saveOtp(String email, String otp);

    @Modifying
    @Query(value = """
            update accounts set password = :password
                            where otp = :otp and otp_expiry > now()
            """, nativeQuery = true)
    int changePassword(String otp, String password);

}
