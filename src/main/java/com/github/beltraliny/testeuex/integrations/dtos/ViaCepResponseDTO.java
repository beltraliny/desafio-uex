package com.github.beltraliny.testeuex.integrations.dtos;

public record ViaCepResponseDTO(String cep, String logradouro, String complemento, String bairro,
                                String localidade, String uf, String ibge, String gia, String ddd, String siafi) {
}
