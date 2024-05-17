package com.techlambdas.delearmanagementapp.service;



import com.techlambdas.delearmanagementapp.model.Config;
import com.techlambdas.delearmanagementapp.request.ConfigReq;

import java.util.List;

public interface ConfigService {
    List<Config> findAll();

    Config save(ConfigReq configReq);

    Config findConfigByConfigId(String configId);

    Config updateConfig(String configId,ConfigReq configReq);


    Config addConfigValue(String configId, String configValue);

    Config removeConfigValue(String configId, String configValue);


}
