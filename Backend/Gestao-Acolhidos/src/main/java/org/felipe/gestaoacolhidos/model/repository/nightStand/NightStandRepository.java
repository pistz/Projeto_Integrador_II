package org.felipe.gestaoacolhidos.model.repository.nightStand;

import org.felipe.gestaoacolhidos.model.domain.entity.NightStand.NightStand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NightStandRepository extends JpaRepository<NightStand, UUID> {
}
