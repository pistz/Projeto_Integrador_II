package org.felipe.gestaoacolhidos.model.domain.entity.NightStand;

import jakarta.persistence.*;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "db_acolhimento")
public class NightStand {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false, name = "acolhidos_dia")
    @OneToMany
    private List<Hosted> hosteds;
}
