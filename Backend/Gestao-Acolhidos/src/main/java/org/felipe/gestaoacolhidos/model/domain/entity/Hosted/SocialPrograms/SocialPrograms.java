package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.SocialPrograms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "db_programas_sociais")
@AllArgsConstructor
@Data
public class SocialPrograms {

    @Id
    private UUID id;

    @Column(nullable = false, name = "municipal_passe_deficiente")
    private boolean hasPasseDeficiente;

    @Column(nullable = false, name = "municipal_passe_idoso")
    private boolean hasPasseIdoso;

    @Column(nullable = false, name = "estadual_renda_cidada")
    private boolean hasRendaCidada;

    @Column(nullable = false, name = "estadual_acao_jovem")
    private boolean hasAcaoJovem;

    @Column(nullable = false, name = "estadual_viva_leite")
    private boolean hasVivaLeite;

    @Column(nullable = false, name = "estadual_BPC_LOAS")
    private boolean hasBPC_LOAS;

    @Column(nullable = false, name = "federal_bolsa_familia")
    private boolean hasBolsaFamilia;

    @Column(nullable = false, name = "federal_PETI")
    private boolean hasPETI;

    @Column(name = "outros_programas")
    private String others;

    @Column(name = "tempo_inserido")
    private int howLong;

    @Column(name = "valor_recebido")
    private BigDecimal wage;

    public SocialPrograms(){
        this.wage = BigDecimal.ZERO;
    }

}
