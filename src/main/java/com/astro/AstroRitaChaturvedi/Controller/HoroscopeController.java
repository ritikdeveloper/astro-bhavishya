package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.Model.HoroscopeModel;
import com.astro.AstroRitaChaturvedi.Service.HoroscopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horoscope")
@CrossOrigin(origins = "*")
public class HoroscopeController {
    
    @Autowired
    private HoroscopeService horoscopeService;
    
    @GetMapping("/today/{zodiacSign}")
    public ResponseEntity<HoroscopeModel> getTodayHoroscope(@PathVariable String zodiacSign) {
        HoroscopeModel horoscope = horoscopeService.getTodayHoroscope(zodiacSign);
        return ResponseEntity.ok(horoscope);
    }
    
    @GetMapping("/weekly/{zodiacSign}")
    public ResponseEntity<List<HoroscopeModel>> getWeeklyHoroscope(@PathVariable String zodiacSign) {
        List<HoroscopeModel> horoscopes = horoscopeService.getWeeklyHoroscope(zodiacSign);
        return ResponseEntity.ok(horoscopes);
    }
    
    @PostMapping
    public ResponseEntity<HoroscopeModel> createHoroscope(@RequestBody HoroscopeModel horoscope) {
        HoroscopeModel created = horoscopeService.createHoroscope(horoscope);
        return ResponseEntity.ok(created);
    }
}