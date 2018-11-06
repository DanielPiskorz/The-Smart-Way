package pl.danielpiskorz.thesmartway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.danielpiskorz.thesmartway.model.Role;
import pl.danielpiskorz.thesmartway.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}