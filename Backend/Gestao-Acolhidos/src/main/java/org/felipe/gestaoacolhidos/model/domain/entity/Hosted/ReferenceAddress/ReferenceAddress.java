package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.ReferenceAddress;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hosted_endereco_referencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceAddress {

    @Id
    private UUID id;

    @Column(name = "rua_referencia")
    private String street;

    @Column(name = "cidade_referencia")
    private String city;

    @Column(name = "bairro_referencia")
    private String neighborhood;

    @Column(name = "numero_casa")
    private int number;

    @Column(name = "cep_referencia")
    private String cep;

    @Column(name = "telefone_referencia")
    private int phoneNumber;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;

}
