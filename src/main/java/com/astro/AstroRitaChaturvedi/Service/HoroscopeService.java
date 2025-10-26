package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.HoroscopeModel;
import com.astro.AstroRitaChaturvedi.Repository.HoroscopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HoroscopeService {
    
    @Autowired
    private HoroscopeRepository horoscopeRepository;
    
    public HoroscopeModel getTodayHoroscope(String zodiacSign) {
        LocalDate today = LocalDate.now();
        Optional<HoroscopeModel> horoscope = horoscopeRepository.findByZodiacSignAndDate(HoroscopeModel.ZodiacSign.valueOf(zodiacSign.toUpperCase()), today);
        
        if (horoscope.isPresent()) {
            return horoscope.get();
        }
        
        // Create today's horoscope if not exists
        return createTodayHoroscope(zodiacSign);
    }
    
    public List<HoroscopeModel> getWeeklyHoroscope(String zodiacSign) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);
        return horoscopeRepository.findByZodiacSignAndDateBetween(HoroscopeModel.ZodiacSign.valueOf(zodiacSign.toUpperCase()), startDate, endDate);
    }
    
    public HoroscopeModel createHoroscope(HoroscopeModel horoscope) {
        return horoscopeRepository.save(horoscope);
    }
    
    private HoroscopeModel createTodayHoroscope(String zodiacSign) {
        HoroscopeModel horoscope = new HoroscopeModel();
        horoscope.setZodiacSign(HoroscopeModel.ZodiacSign.valueOf(zodiacSign.toUpperCase()));
        horoscope.setDate(LocalDate.now());
        horoscope.setDailyPrediction(generatePrediction(zodiacSign));
        horoscope.setLuckyNumber(generateLuckyNumber());
        horoscope.setLuckyColor(generateLuckyColor());
        horoscope.setLoveLife("Love is in the air today");
        horoscope.setCareer("Career opportunities await");
        horoscope.setHealth("Take care of your health");
        horoscope.setFinance("Financial stability is key");
        
        return horoscopeRepository.save(horoscope);
    }
    
    private String generatePrediction(String zodiacSign) {
        String[] predictions = {
            "Today brings new opportunities for growth and self-discovery. Trust your intuition and embrace positive changes.",
            "Focus on relationships and communication today. Your words have power to heal and inspire others.",
            "Financial matters require attention. Make wise decisions and avoid impulsive purchases.",
            "Your creativity is at its peak today. Express yourself through art, music, or writing.",
            "Health and wellness should be your priority. Take time for self-care and relaxation.",
            "Career opportunities may present themselves. Stay alert and be ready to take action.",
            "Family and home life bring joy and comfort today. Spend quality time with loved ones.",
            "Learning and education are favored. Consider taking up a new skill or hobby."
        };
        return predictions[(int) (Math.random() * predictions.length)];
    }
    
    private String generateLuckyNumber() {
        return String.valueOf((int) (Math.random() * 9) + 1);
    }
    
    private String generateLuckyColor() {
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink", "Golden", "Silver", "White"};
        return colors[(int) (Math.random() * colors.length)];
    }
    
    private int generateRating() {
        return (int) (Math.random() * 5) + 1;
    }
}