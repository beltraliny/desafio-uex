package com.github.beltraliny.testeuex.integrations.dtos;

import com.github.beltraliny.testeuex.integrations.dtos.children.GoogleMapsChildrenResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record GoogleMapsResponseDTO(List<GoogleMapsChildrenResponseDTO> results, String status) {

    public Map<String, String> findLatLong() {
        Optional<GoogleMapsChildrenResponseDTO> results = results().stream().findFirst();
        if (results.isEmpty()) return null;

        return results.get().findLatLong();
    }
}
