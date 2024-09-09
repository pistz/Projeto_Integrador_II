package org.felipe.gestaoacolhidos.model.domain.entity.NightReception;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "db_acolhimento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NightReception {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private LocalDate eventDate;

    @Column(nullable = false, name = "updated_by")
    private String updatedBy;

    @OneToMany
    private List<Hosted> hosteds;

}
