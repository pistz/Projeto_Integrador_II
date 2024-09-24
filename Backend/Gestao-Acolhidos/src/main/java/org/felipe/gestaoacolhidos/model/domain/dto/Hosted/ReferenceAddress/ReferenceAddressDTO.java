package org.felipe.gestaoacolhidos.model.domain.dto.Hosted.ReferenceAddress;

public record ReferenceAddressDTO(
        String street,
        String city,
        String neighborhood,
        int number,
        String cep,
        long phoneNumber
) {
}
