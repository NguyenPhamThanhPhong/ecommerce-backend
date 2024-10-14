package ecommerce.api.repository;

import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<Account, UUID> {
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

    @Query("select a,b from Account a join Profile b on a.id=b.id where a.email = :data or a.loginId = :data")
    Account findByEmailOrLoginId(String data);

    @Modifying
    @Transactional
    int deleteAccountById(UUID id);

    @Modifying
    @Transactional
    @Query("update Account a set a.deletedAt = CURRENT_TIMESTAMP where a.id = :id")
    int updateAccountDeletedAt(UUID id);
}
