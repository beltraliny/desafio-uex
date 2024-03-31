package com.github.beltraliny.testeuex.integrations.dtos;

import com.github.beltraliny.testeuex.integrations.dtos.children.GoogleMapsChildrenResponseDTO;

import java.util.List;

public record GoogleMapsResponseDTO(List<GoogleMapsChildrenResponseDTO> results, String status) {
}
