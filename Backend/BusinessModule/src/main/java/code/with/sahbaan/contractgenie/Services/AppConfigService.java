package code.with.sahbaan.contractgenie.Services;


import code.with.sahbaan.contractgenie.Entities.AppConfigs;

public interface AppConfigService {

    AppConfigs getAppConfigsByName(String name);
}
