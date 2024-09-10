package org.felipe.gestaoacolhidos.model.repository.hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HostedRepository extends JpaRepository<Hosted, UUID> {

    List<Hosted> findAll();
    Optional<Hosted> findById(UUID id);
    boolean existsBySocialSecurityNumber(String CPF);

    @Query("SELECT nr.eventDate FROM NightReception nr JOIN nr.hosteds h WHERE h.id = :hostedId ")
    List<LocalDate> queryHostedByNighReception(@Param("hostedId") UUID hostedId);
}
