package com.github.beltraliny.testeuex.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.beltraliny.testeuex.integrations.dtos.ViaCepResponseDTO;
import com.github.beltraliny.testeuex.integrations.managers.IntegrationManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ViaCepService {

    private final IntegrationManagerService integrationManagerService;

    public ViaCepService(IntegrationManagerService integrationManagerService) {
        this.integrationManagerService = integrationManagerService;
    }

    public ViaCepResponseDTO findAddressByPostalCode(String postalCode) {
        try {
            String url = "https://viacep.com.br/ws/" + postalCode + "/json";

            String response = this.integrationManagerService.get(url);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, ViaCepResponseDTO.class);

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
