package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Attendance;

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
@Table(name = "db_estadia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    @Id
    private UUID id;

    @Column(name = "estadia_id", nullable = false)
    private UUID nightReceptionId;

    @Column(name = "data_estadia", unique = true, nullable = false)
    private LocalDate date;

}
