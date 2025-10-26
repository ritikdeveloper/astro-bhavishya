package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.HoroscopeModel;
import com.astro.AstroRitaChaturvedi.Repository.HoroscopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class DataSeedingService implements CommandLineRunner {
    
    @Autowired
    private HoroscopeRepository horoscopeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Disabled to prevent errors on startup
        // seedHoroscopeData();
    }
    
    private void seedHoroscopeData() {
        List<String> zodiacSigns = Arrays.asList(
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
        );
        
        LocalDate today = LocalDate.now();
        
        for (String sign : zodiacSigns) {
            if (horoscopeRepository.findByZodiacSignAndDate(HoroscopeModel.ZodiacSign.valueOf(sign.toUpperCase()), today).isEmpty()) {
                HoroscopeModel horoscope = new HoroscopeModel();
                horoscope.setZodiacSign(HoroscopeModel.ZodiacSign.valueOf(sign.toUpperCase()));
                horoscope.setDate(today);
                horoscope.setDailyPrediction(generatePredictionForSign(sign));
                horoscope.setLuckyNumber(String.valueOf((int) (Math.random() * 9) + 1));
                horoscope.setLuckyColor(generateLuckyColor());
                horoscope.setLoveLife("Love is favorable today");
                horoscope.setCareer("Career growth is possible");
                horoscope.setHealth("Maintain good health habits");
                horoscope.setFinance("Financial planning is important");
                
                horoscopeRepository.save(horoscope);
            }
        }
    }
    
    private String generatePredictionForSign(String sign) {
        switch (sign) {
            case "Aries": return "Today brings new opportunities for leadership. Your natural energy will attract positive outcomes.";
            case "Taurus": return "Focus on stability and practical matters. Your patience will be rewarded with lasting success.";
            case "Gemini": return "Communication is key today. Share your ideas and connect with others for mutual benefit.";
            case "Cancer": return "Trust your intuition and nurture your relationships. Home and family bring comfort.";
            case "Leo": return "Your creativity shines bright today. Express yourself boldly and embrace the spotlight.";
            case "Virgo": return "Attention to detail pays off. Organize your thoughts and plans for maximum efficiency.";
            case "Libra": return "Seek balance in all areas of life. Harmony and cooperation lead to positive outcomes.";
            case "Scorpio": return "Deep transformation is possible today. Embrace change and trust your inner wisdom.";
            case "Sagittarius": return "Adventure calls to you. Expand your horizons through learning and exploration.";
            case "Capricorn": return "Hard work and determination bring recognition. Stay focused on your long-term goals.";
            case "Aquarius": return "Innovation and originality set you apart. Think outside the box for unique solutions.";
            case "Pisces": return "Your compassion and creativity inspire others. Trust your dreams and intuitive insights.";
            default: return "Today brings new opportunities for growth and self-discovery.";
        }
    }
    
    private String generateLuckyColor() {
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink", "Golden", "Silver", "White"};
        return colors[(int) (Math.random() * colors.length)];
    }
}