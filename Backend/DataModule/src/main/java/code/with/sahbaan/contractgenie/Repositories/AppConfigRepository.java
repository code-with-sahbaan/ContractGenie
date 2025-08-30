package code.with.sahbaan.contractgenie.Repositories;

import code.with.sahbaan.contractgenie.Entities.AppConfigs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfigs, Long> {

    AppConfigs findByName(String name);

}
