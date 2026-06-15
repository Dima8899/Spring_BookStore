package mate.academy.library.dao;

import java.util.Optional;
import mate.academy.library.model.Role;
import mate.academy.library.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
