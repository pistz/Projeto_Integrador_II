package org.felipe.gestaoacolhidos.model.dto.Hosted.ReferenceAddress;

public record ReferenceAddressDTO(
        String street,
        String city,
        String neighborhood,
        int number,
        String cep,
        int phoneNumber
) {
}
