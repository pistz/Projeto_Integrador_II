package org.felipe.gestaoacolhidos.model.repository.nightReception;

import org.felipe.gestaoacolhidos.model.domain.entity.NightReception.NightReception;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NightReceptionRepository extends JpaRepository<NightReception, UUID> {

    Optional<NightReception> findById(UUID uuid);

    Optional<NightReception> findByEventDate(LocalDate eventDate);

    List<NightReception> findAll(Sort sort);

    @Query("SELECT nr FROM NightReception nr WHERE EXTRACT(month from nr.eventDate) = :month AND EXTRACT( year from nr.eventDate) = :year")
    List<NightReception> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT nr FROM NightReception nr WHERE EXTRACT( year from nr.eventDate) = :year")
    List<NightReception> findByYear(@Param("year") int year);
}
