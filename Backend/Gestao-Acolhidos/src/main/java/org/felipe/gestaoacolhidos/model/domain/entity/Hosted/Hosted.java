package org.felipe.gestaoacolhidos.model.domain.entity.Hosted;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.CustomTreatments.CustomTreatments;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Documents.Documents;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.MedicalRecord.MedicalRecord;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.PoliceReport.PoliceReport;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.ReferenceAddress.ReferenceAddress;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SituationalRisk.SituationalRisk;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SocialPrograms.SocialPrograms;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

/**
 * Classe referente à ficha de cadastro do acolhido
 * Um acolhido é o nome que se dá ao usuário do Albergue
 * @Author Felipe
 * */
@Entity
@Table(name = "db_hosted")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hosted {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private UUID id;

    @Column(name = "prontuario_papel")
    private long paperTrail;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(name = "aniversario")
    private LocalDate birthDay;

    @Column(name = "nome_pai")
    private String fathersName;

    @Column(nullable = false, name = "nome_mae")
    private String mothersName;

    @Column(name = "profissao")
    private String occupation;

    @Column(name = "cidade_origem")
    private String cityOrigin;

    @Column(name = "estado_origem")
    private String stateOrigin;

    //Relacionamentos tabelas Adjacentes

    @OneToOne(cascade = CascadeType.ALL)
    private Documents documents;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PoliceReport> policeReport;

    @OneToOne(cascade = CascadeType.ALL)
    private ReferenceAddress referenceAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private SituationalRisk situationalRisk;

    @OneToOne(cascade = CascadeType.ALL)
    private SocialPrograms socialPrograms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecord;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomTreatments> customTreatments;

    //Informativos

    @Column(nullable = false, name = "created_at")
    private LocalDate createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDate updatedAt;

    public int getAge() {
        if(birthDay == null) {
            return 0;
        }
        LocalDate now = LocalDate.now();
        var age = Period.between(birthDay, now).getYears();
        return age;
    }
}
