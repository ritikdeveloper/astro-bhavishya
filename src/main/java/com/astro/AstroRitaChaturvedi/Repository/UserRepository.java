package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
    Optional<UserModel> findByEmail(String email);
    long countByRole(UserModel.UserRole role);
}
