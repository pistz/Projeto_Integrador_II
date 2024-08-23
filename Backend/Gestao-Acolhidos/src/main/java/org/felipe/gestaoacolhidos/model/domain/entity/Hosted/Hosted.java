package org.felipe.gestaoacolhidos.model.domain.entity.Hosted;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * @Author Felipe
 * */
@Entity
@Table(name = "db_hosted")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Column(name = "db_documentos")
    @OneToOne(cascade = CascadeType.ALL)
    private Documents documents;

    @Column(name = "db_boletim_ocorrencia")
    @OneToMany(cascade = CascadeType.ALL)
    private List<PoliceReport> policeReport;

    @Column(name = "db_endereco_referencia")
    @OneToOne(cascade = CascadeType.ALL)
    private ReferenceAddress referenceAddress;

    @Column(name = "db_modalidade_situacao_risco")
    @OneToOne(cascade = CascadeType.ALL)
    private SituationalRisk situationalRisk;

    @Column(name = "db_programas_sociais")
    @OneToOne(cascade = CascadeType.ALL)
    private SocialPrograms socialPrograms;

    @Column(name = "db_queixas_saude")
    @OneToMany(cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecord;

    @Column(name = "db_historico_procedimentos")
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
