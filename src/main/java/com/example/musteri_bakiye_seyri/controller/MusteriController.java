package com.example.musteri_bakiye_seyri.controller;

import com.example.musteri_bakiye_seyri.model.Musteri;
import com.example.musteri_bakiye_seyri.service.MusteriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class MusteriController {

    @Autowired
    private MusteriService musteriService;

    @GetMapping("/musteri/list")
    public String getMusteriList(Model model) {
        List<Musteri> musteriler = musteriService.getAllMusteriler();
        model.addAttribute("musteriler", musteriler);
        return "musteriList";
    }

    @GetMapping("/musteri/max-borc-tarihi")
    public String getMaxBorcluTarihi(@RequestParam Long musteriId, Model model) {
        Date maxBorcluTarihi = musteriService.getMaxBorcluTarihi(musteriId);
        Musteri musteri = musteriService.getMusteriById(musteriId);
        
        model.addAttribute("musteri", musteri);
        model.addAttribute("maxBorcluTarihi", maxBorcluTarihi);
        return "musteri";
    }
}
