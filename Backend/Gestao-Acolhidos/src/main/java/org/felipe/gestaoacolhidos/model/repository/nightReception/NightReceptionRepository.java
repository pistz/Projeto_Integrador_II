package org.felipe.gestaoacolhidos.model.repository.nightReception;

import org.felipe.gestaoacolhidos.model.domain.entity.NightReception.NightReception;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NightReceptionRepository extends JpaRepository<NightReception, UUID> {

    Optional<NightReception> findById(UUID uuid);
    Optional<NightReception> findByEventDate(LocalDate eventDate);
    List<NightReception> findAll();
}
