package ecommerce.api.repository;

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

    @Query("select a from Account a left join fetch a.profile where a.code = :code")
    Optional<Account> findByCode(long code);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    List<Account> findAll();

    @EntityGraph(attributePaths = {"profile"})
    Page<Account> findAll(@NotNull Specification<Account> spec, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Profile p set p.addresses = :addresses, p.primaryAddress = :primaryAddress where p.id = :id")
    int updateAddresses(UUID id, Map<String,String> addresses, String primaryAddress);

    @Modifying
    @Transactional
    @Query(value = """
                update accounts set email = :#{#account.email}, role = :#{#account.role}, 
                                    password = :#{#account.password}, 
                                    enable_date = :#{#account.enableDate}, disable_date = :#{#account.disableDate}, 
                                    is_verified = :#{#account.isVerified} where id = :#{#account.id}
            """,nativeQuery = true)
    void updateAccount(Account account);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    Optional<Account> findOne(Specification specification);

}
