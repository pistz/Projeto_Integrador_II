package org.felipe.gestaoacolhidos.model.repository.capacity;

import org.felipe.gestaoacolhidos.model.domain.entity.Capacity.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CapacityReposity extends JpaRepository<Capacity, UUID> {

    @Query("SELECT ic FROM Capacity ic ORDER BY ic.updatedAt DESC LIMIT 1")
    Optional<Capacity> findCurrentConfig();

    @Query("SELECT ic.maxCapacity FROM Capacity ic ORDER BY ic.updatedAt DESC LIMIT 1")
    Optional<Integer> findCurrentMaxCapacity();
}
