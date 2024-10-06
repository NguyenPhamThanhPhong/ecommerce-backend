package ecommerce.api.repository;

import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<Account,UUID> {
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

    Account findByEmailOrLoginId(String s, String s1);
}
