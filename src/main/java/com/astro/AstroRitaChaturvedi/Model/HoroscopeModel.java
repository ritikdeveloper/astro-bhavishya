package com.astro.AstroRitaChaturvedi.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "horoscopes")
public class HoroscopeModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID horoscopeId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZodiacSign zodiacSign;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false, length = 2000)
    private String dailyPrediction;
    
    @Column(length = 500)
    private String loveLife;
    
    @Column(length = 500)
    private String career;
    
    @Column(length = 500)
    private String health;
    
    @Column(length = 500)
    private String finance;
    
    private String luckyNumber;
    private String luckyColor;
    
    public enum ZodiacSign {
        ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO, 
        LIBRA, SCORPIO, SAGITTARIUS, CAPRICORN, AQUARIUS, PISCES
    }
    
    // Constructors
    public HoroscopeModel() {}
    
    // Getters and Setters
    public UUID getHoroscopeId() { return horoscopeId; }
    public void setHoroscopeId(UUID horoscopeId) { this.horoscopeId = horoscopeId; }
    
    public ZodiacSign getZodiacSign() { return zodiacSign; }
    public void setZodiacSign(ZodiacSign zodiacSign) { this.zodiacSign = zodiacSign; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getDailyPrediction() { return dailyPrediction; }
    public void setDailyPrediction(String dailyPrediction) { this.dailyPrediction = dailyPrediction; }
    
    public String getLoveLife() { return loveLife; }
    public void setLoveLife(String loveLife) { this.loveLife = loveLife; }
    
    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }
    
    public String getHealth() { return health; }
    public void setHealth(String health) { this.health = health; }
    
    public String getFinance() { return finance; }
    public void setFinance(String finance) { this.finance = finance; }
    
    public String getLuckyNumber() { return luckyNumber; }
    public void setLuckyNumber(String luckyNumber) { this.luckyNumber = luckyNumber; }
    
    public String getLuckyColor() { return luckyColor; }
    public void setLuckyColor(String luckyColor) { this.luckyColor = luckyColor; }
}