package com.virtya.CryptoMarket.repository;

import com.virtya.CryptoMarket.entity.OurUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<OurUser,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsBySecretKey(String secretKey);
    Optional<OurUser> findBySecretKey(String secretKey);
    @Query("select u from OurUser u where u.email = ?2 OR u.username = ?1")
    OurUser findByUsernameOrEmail(String userName, String email);
}
