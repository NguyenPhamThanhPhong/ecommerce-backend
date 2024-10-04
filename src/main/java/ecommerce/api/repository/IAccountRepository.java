package ecommerce.api.repository;

import ecommerce.api.entity.user.Account;
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
            update Account a set a.profile.fullName = :fullName, a.profile.dateOfBirth = :birthDate,
             a.profile.phone = :phone
            where a.id = :id
            """
    )
    void updateProfile(UUID id, String fullName, Date birthDate, String phone);

    Account findByEmailOrLoginId(String s, String s1);
}
