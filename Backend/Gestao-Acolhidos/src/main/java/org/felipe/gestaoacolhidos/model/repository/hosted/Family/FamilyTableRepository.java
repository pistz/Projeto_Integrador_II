package org.felipe.gestaoacolhidos.model.repository.hosted.Family;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.FamilyTable.FamilyTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FamilyTableRepository extends JpaRepository<FamilyTable, UUID> {
}
