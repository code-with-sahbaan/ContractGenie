package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.AppConfigs;
import code.with.sahbaan.contractgenie.Repositories.AppConfigRepository;
import code.with.sahbaan.contractgenie.Services.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl extends GenericServiceImpl<AppConfigs> implements AppConfigService {


    public AppConfigServiceImpl() {
        super(AppConfigs.class);
    }

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Override
    public AppConfigs getAppConfigsByName(String name) {
        return appConfigRepository.findByName(name);
    }
}
