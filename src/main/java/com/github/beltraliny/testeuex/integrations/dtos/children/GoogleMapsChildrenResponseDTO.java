package com.github.beltraliny.testeuex.integrations.dtos.children;

import java.util.List;

public record GoogleMapsChildrenResponseDTO(List<AddressComponent> address_components, String formatted_address,
                                            Geometry geometry, String place_id, List<String> types) {}

record AddressComponent(String long_name, String short_name, List<String> types) {}

record Geometry(Bounds bounds, Location location, String location_type, Viewport viewport) {}

record Bounds(Location northeast, Location southwest) {}

record Location(String lat, String lng) {}

record Viewport(Location northeast, Location southwest) {}


