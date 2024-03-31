package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.integrations.ViaCepManagerService;
import com.github.beltraliny.testeuex.integrations.dtos.ViaCepResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
public class IntegrationsController {

    private final ViaCepManagerService viaCepManagerService;

    public IntegrationsController(ViaCepManagerService viaCepManagerService) {
        this.viaCepManagerService = viaCepManagerService;
    }

    @RequestMapping("/postal-codes/{postalCode}")
    public ResponseEntity<ViaCepResponseDTO> findAdressByPostalCode(@PathVariable String postalCode) {
        ViaCepResponseDTO viaCepResponseDTO = this.viaCepManagerService.get(postalCode);
        return ResponseEntity.ok(viaCepResponseDTO);
    }
}
