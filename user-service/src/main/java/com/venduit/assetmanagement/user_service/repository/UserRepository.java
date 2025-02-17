package com.venduit.assetmanagement.user_service.repository;

import com.venduit.assetmanagement.user_service.model.Role;
import com.venduit.assetmanagement.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String userName);

    Optional<User> findByVerificationOtpAndEmail(String verificationOtp, String email);

    List<User> findAllByRole(Role role);

    Optional<User> findByEmailAndResetOtp(String email, String resetToken);
}
