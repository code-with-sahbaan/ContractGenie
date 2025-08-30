package code.with.sahbaan.contractgenie.Repositories;

import code.with.sahbaan.contractgenie.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

}
