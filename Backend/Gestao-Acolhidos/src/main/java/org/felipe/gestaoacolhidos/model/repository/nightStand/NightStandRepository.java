package org.felipe.gestaoacolhidos.model.repository.nightStand;

import org.felipe.gestaoacolhidos.model.domain.entity.NightStand.NightStand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NightStandRepository extends JpaRepository<NightStand, UUID> {

    Optional<NightStand> findById(UUID uuid);
    Optional<NightStand> findByEventDate(LocalDate eventDate);
    List<NightStand> findAll();
}
