package com.bdd.stylists;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(StylistController.URL)
public class StylistController {
    public static final String URL = "/stylists";
    private StylistService stylistService;

    public StylistController(StylistService stylistService) {
        this.stylistService = stylistService;
    }

    @PostMapping
    public ResponseEntity<Stylist> addStylist(@RequestBody Stylist stylist) {
        return stylistService.addStylist(stylist);
    }
}
