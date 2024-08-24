package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.BirthCertificate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "hosted_certidao_nascimento")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BirthCertificate {

    @Id
    private UUID id;

    @Column(name = "matricula")
    private long certificateNumber;

    @Column(name = "folhas_livro")
    private String sheets;

    @Column(name = "livro")
    private int book;
}
