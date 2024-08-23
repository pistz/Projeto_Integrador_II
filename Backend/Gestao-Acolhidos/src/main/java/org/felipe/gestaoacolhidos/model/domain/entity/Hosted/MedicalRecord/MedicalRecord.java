package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.MedicalRecord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "db_saude")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalRecord {

    @Id
    private UUID id;

    @Column(name = "queixas")
    private String complaints;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
