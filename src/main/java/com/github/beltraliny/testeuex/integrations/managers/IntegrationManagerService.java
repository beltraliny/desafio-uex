package com.github.beltraliny.testeuex.integrations.managers;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class IntegrationManagerService {

    public String get(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            return this.buildResponse(reader);
        } catch (Exception exception) {
            return null;
        }
    }

    private String buildResponse(BufferedReader reader) {
        try {
            StringBuilder response = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
