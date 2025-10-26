package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Model.WalletModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletModel, UUID> {
    Optional<WalletModel> findByUser(UserModel user);
}
