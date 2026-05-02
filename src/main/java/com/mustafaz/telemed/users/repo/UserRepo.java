package com.mustafaz.telemed.users.repo;

import com.mustafaz.telemed.enums.AuthProvider;
import com.mustafaz.telemed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.authProvider = ?2 WHERE u.id = ?1")
    void updateAuthenticationType(Long userId, AuthProvider authProvider);
}
