package org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "db_estadia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    @Id
    private UUID id;

    @Column(name = "data_estadia")
    private LocalDateTime date;
}
