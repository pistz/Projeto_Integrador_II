package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyComposition;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;

import java.util.UUID;

@Entity
@Table(name = "tabela_familiar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FamilyTable {

    @Id
    private UUID id;

    @Column(name = "nome_do_familiar")
    private String name;

    @Column(name = "idade_do_familiar")
    private int age;

    @Column(name = "genero_familiar")
    private Gender gender;

    @Column(name = "estado_civil")
    private MaritalStatus maritalStatus;

    @Column(name = "escolaridade")
    private Education education;

    @Column(name = "ocupacao_profissional")
    private String ocupation;
}
