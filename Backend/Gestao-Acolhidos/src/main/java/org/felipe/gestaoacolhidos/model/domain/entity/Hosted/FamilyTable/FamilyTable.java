package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.enums.education.Education;
import org.felipe.gestaoacolhidos.model.domain.enums.gender.Gender;
import org.felipe.gestaoacolhidos.model.domain.enums.maritalStatus.MaritalStatus;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "hosted_tabela_familiar")
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "estado_civil")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "escolaridade")
    @Enumerated(EnumType.STRING)
    private Education education;

    @Column(name = "ocupacao_profissional")
    private String occupation;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyTable that = (FamilyTable) o;
        return Objects.equals(name, that.name) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender);
    }
}
