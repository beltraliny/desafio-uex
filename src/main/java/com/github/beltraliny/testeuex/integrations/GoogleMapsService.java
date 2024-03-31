package com.github.beltraliny.testeuex.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.beltraliny.testeuex.integrations.dtos.GoogleMapsResponseDTO;
import com.github.beltraliny.testeuex.integrations.managers.IntegrationManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class GoogleMapsService {

    @Value("${integration.google.maps.api.key}")
    private String apiKey;

    private final IntegrationManagerService integrationManagerService;

    public GoogleMapsService(IntegrationManagerService integrationManagerService) {
        this.integrationManagerService = integrationManagerService;
    }

    public Map<String, String> findLatLogByAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + this.apiKey;

            String response = this.integrationManagerService.get(url);

            ObjectMapper objectMapper = new ObjectMapper();
            GoogleMapsResponseDTO googleMapsResponseDTO = objectMapper.readValue(response, GoogleMapsResponseDTO.class);

            return googleMapsResponseDTO.findLatLong();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}