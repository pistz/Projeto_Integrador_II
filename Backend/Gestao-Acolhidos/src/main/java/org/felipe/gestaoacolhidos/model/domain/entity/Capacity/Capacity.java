package org.felipe.gestaoacolhidos.model.domain.entity.Capacity;

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
@Table(name = "db_capacidade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capacity {

    @Id
    private UUID id;

    @Column(nullable = false, name = "max_capacidade")
    private int maxCapacity;

    @Column(nullable = false, name = "updated_at")
    private LocalDate updatedAt;

}
