package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.integrations.ViaCepService;
import com.github.beltraliny.testeuex.integrations.dtos.ViaCepResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
public class IntegrationsController {

    private final ViaCepService viaCepService;

    public IntegrationsController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/postal-codes/{postalCode}")
    public ResponseEntity<ViaCepResponseDTO> findAddressByPostalCode(@PathVariable String postalCode) {
        ViaCepResponseDTO viaCepResponseDTO = this.viaCepService.findAddressByPostalCode(postalCode);
        return ResponseEntity.ok(viaCepResponseDTO);
    }
}
