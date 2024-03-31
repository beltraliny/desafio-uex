package com.github.beltraliny.testeuex.models.dtos;

public record ContactDTO(String name, String cpf, String phoneNumber, String street, String number,
                         String neighborhood, String state, String country, String complement, String postalCode,
                         String latitude, String longitude) {
}
