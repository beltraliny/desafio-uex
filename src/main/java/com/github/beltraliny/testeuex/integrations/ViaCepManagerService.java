package com.github.beltraliny.testeuex.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.beltraliny.testeuex.integrations.dtos.ViaCepResponseDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class ViaCepManagerService {

    public ViaCepResponseDTO get(String postalCode) {
        try {
            String url = "https://viacep.com.br/ws/" + postalCode + "/json";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            ViaCepResponseDTO viaCepResponseDTO = objectMapper.readValue(response.toString(), ViaCepResponseDTO.class);

            return viaCepResponseDTO;
        } catch (Exception exception) {
            return null;
        }
    }
}
