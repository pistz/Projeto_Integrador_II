package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.CustomTreatments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hosted_plano_personalizado")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomTreatments {

    @Id
    private UUID id;

    @Column(name = "procedimento")
    private String procedure;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate updatedAt;

}
