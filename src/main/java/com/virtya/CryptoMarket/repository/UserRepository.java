package com.virtya.CryptoMarket.repository;

import com.virtya.CryptoMarket.entity.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<OurUser,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsBySecretKey(String secretKey);
    Optional<OurUser> findBySecretKey(String secretKey);
}
