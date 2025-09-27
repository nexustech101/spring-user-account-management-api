package com.archtech.store.repository;

import com.archtech.store.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    // Spring Data JPA automatically gives you CRUD methods
    
    // Custom query methods
    Optional<UserAccount> findByUserName(String userName);
    Optional<UserAccount> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
