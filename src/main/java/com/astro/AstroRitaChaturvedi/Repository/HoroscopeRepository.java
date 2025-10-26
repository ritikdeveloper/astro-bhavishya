package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.HoroscopeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HoroscopeRepository extends JpaRepository<HoroscopeModel, UUID> {
    Optional<HoroscopeModel> findByZodiacSignAndDate(HoroscopeModel.ZodiacSign zodiacSign, LocalDate date);
    List<HoroscopeModel> findByZodiacSignAndDateBetween(HoroscopeModel.ZodiacSign zodiacSign, LocalDate startDate, LocalDate endDate);
}