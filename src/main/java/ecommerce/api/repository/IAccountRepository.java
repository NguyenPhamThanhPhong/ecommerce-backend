package ecommerce.api.repository;

import ecommerce.api.entity.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account,String> {
}
