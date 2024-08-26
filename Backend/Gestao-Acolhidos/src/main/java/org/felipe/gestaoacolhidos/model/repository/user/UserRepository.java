package org.felipe.gestaoacolhidos.model.repository.user;

import org.felipe.gestaoacolhidos.model.domain.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Override
    void deleteById(UUID uuid);
}
