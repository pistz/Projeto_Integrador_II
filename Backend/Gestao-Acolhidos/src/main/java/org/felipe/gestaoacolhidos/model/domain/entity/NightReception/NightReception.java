package org.felipe.gestaoacolhidos.model.domain.entity.NightReception;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "db_acolhimento_hosteds",
            joinColumns = @JoinColumn(name = "night_reception_id"),
            inverseJoinColumns = @JoinColumn(name = "hosteds_id")
    )
    private List<Hosted> hosteds = new ArrayList<>();

}
