package ecommerce.api.repository;

import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
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


    @Query("""
                select a from Account a left join fetch a.profile where a.email = :data or a.loginId = :data
            """)
    Account findByEmailOrLoginId(String data);

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
    Optional<Account> findById(UUID id);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    List<Account> findAll();

    @Override
    @Query(value = "select a from Account a left join fetch a.profile",
            countQuery = "select count(1) from Account a")
    Page<Account> findAll(Specification specification, Pageable pageable);

    @Override
    @Query("select a from Account a left join fetch a.profile")
    Optional<Account> findOne(Specification  specification);

}
