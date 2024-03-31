package com.github.beltraliny.testeuex.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.beltraliny.testeuex.integrations.dtos.GoogleMapsResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleMapsManagerService {

    @Value("${integration.google.maps.api.key}")
    private String apiKey;

    public GoogleMapsResponseDTO get(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + this.apiKey;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            GoogleMapsResponseDTO googleMapsResponseDTO = objectMapper.readValue(response.toString(), GoogleMapsResponseDTO.class);

            return googleMapsResponseDTO;
        } catch (Exception exception) {
            return null;
        }
    }
}