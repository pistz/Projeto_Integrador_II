package org.felipe.gestaoacolhidos.model.repository.hosted;

import org.felipe.gestaoacolhidos.model.domain.entity.Hosted.Hosted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HostedRepository extends JpaRepository<Hosted, UUID> {

    List<Hosted> findAll();

    Optional<Hosted> findById(UUID id);

    Optional<Hosted> findByPaperTrail(long paperTrail);

    List<Optional<Hosted>> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT h.firstName, h.lastName, d.socialSecurityNumber FROM Hosted h JOIN h.documents d WHERE d.socialSecurityNumber = :cpf")
    Optional<Hosted> findByDocumentCpf(String cpf);
}
