package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.ConfigType;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.exception.InvalidDataException;
import com.techlambdas.delearmanagementapp.model.Config;
import com.techlambdas.delearmanagementapp.repository.ConfigRepository;
import com.techlambdas.delearmanagementapp.request.ConfigReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService{
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Config> findAll(String configId) {
        Query query=new Query();
        List<Config> configList = new ArrayList<>();
        if (configId != null && !configId.isEmpty()) {
            query.addCriteria(Criteria.where("configId").is(configId));
        }
        query.addCriteria(Criteria.where("configType").ne(ConfigType.SYSTEM));
        return mongoTemplate.find(query, Config.class);
    }
    @Override
    public Config save(ConfigReq configReq) {
        try {
            Config config = new Config();
            config.setConfigId(configReq.getConfigId());
            if (configReq.getConfiguration()==null||configReq.getConfiguration().isEmpty())
                throw new InvalidDataException("Cannot Empty The Config Value");
            config.setConfiguration(configReq.getConfiguration());
            config.setConfigType(ConfigType.USER);
            config.setDefaultValue(configReq.getDefaultValue());
            return configRepository.save(config);
        }catch (Exception e){
            throw new InvalidDataException("Invalid Request Check Request Object");
        }
    }

    @Override
    public Config findConfigByConfigId(String configId) {

        Config config= configRepository.findConfigByConfigId(configId);
        if (config==null)
            throw new DataNotFoundException("Config Not Found This ID:"+configId);
        return config;
    }

    @Override
    public Config updateConfig(String configId,ConfigReq configReq) {
        Config config = configRepository.findConfigByConfigId(configId);
        if (config==null)
            throw new DataNotFoundException("Config Not Found This ID:"+configId);
        if (config.getConfigType()==ConfigType.NON_EDIT){
            throw new UnsupportedOperationException("Can't Edit ONLY ADD");
        }
        config.setConfigId(configReq.getConfigId());
        config.setConfiguration(configReq.getConfiguration());
        config.setDefaultValue(configReq.getDefaultValue());
        return configRepository.save(config);
    }

    @Override
    public Config addConfigValue(String configId, String configValue) {
        Config updatedConfig= configRepository.findConfigByConfigId(configId);
        if (updatedConfig==null)
            throw new DataNotFoundException("Config Not Found This ID:"+configId);
        List<String> configuration = updatedConfig.getConfiguration();
        configuration.add(configValue);
        updatedConfig.setConfiguration(configuration);
        return configRepository.save(updatedConfig);
    }

    @Override
    public Config removeConfigValue(String configId, String configValue) {
        Config config = configRepository.findConfigByConfigId(configId);
        if (config==null)
            throw new DataNotFoundException("Config Not Found This ID:"+configId);
        List<String> configuration = config.getConfiguration();
        configuration.remove(configValue);
        config.setConfiguration(configuration);
        return configRepository.save(config);
    }
    @Override
    public String getNextPurchaseNoSequence() {
        Query query = new Query(Criteria.where("configId").is("purchaseNoSeq"));
        Config config = mongoTemplate.findOne(query, Config.class);
        if (config!=null) {
            List<String> regSeqNo = config.getConfiguration();
            int currentSequence = Integer.parseInt(regSeqNo.get(0));
            int nextSeq = currentSequence + 1;
            config.setConfiguration(Collections.singletonList(Integer.toString(nextSeq)));
            mongoTemplate.save(config);
            return String.valueOf(currentSequence);
        }else {
            return null;
        }
    }
    @Override
    public String getNextSalesNoSequence() {
        Query query = new Query(Criteria.where("configId").is("salesNoSeq"));
        Config config = mongoTemplate.findOne(query, Config.class);
        if (config!=null) {
            List<String> regSeqNo = config.getConfiguration();
            int currentSequence = Integer.parseInt(regSeqNo.get(0));
            int nextSeq = currentSequence + 1;
            config.setConfiguration(Collections.singletonList(Integer.toString(nextSeq)));
            mongoTemplate.save(config);
            return String.valueOf(currentSequence);
        }else {
            return null;
        }
    }

    @Override
    public String getNextReceiptNoSequence() {
        Query query = new Query(Criteria.where("configId").is("receiptNoSeq"));
        Config config = mongoTemplate.findOne(query, Config.class);
        if (config!=null) {
            List<String> regSeqNo = config.getConfiguration();
            int currentSequence = Integer.parseInt(regSeqNo.get(0));
            int nextSeq = currentSequence + 1;
            config.setConfiguration(Collections.singletonList(Integer.toString(nextSeq)));
            mongoTemplate.save(config);
            return String.valueOf(currentSequence);
        }else {
            return null;
        }
    }

    @Override
    public String getNextBookingNoSequence() {
        Query query = new Query(Criteria.where("configId").is("bookingNo"));
        Config config = mongoTemplate.findOne(query, Config.class);
        if (config!=null) {
            List<String> regSeqNo = config.getConfiguration();
            int currentSequence = Integer.parseInt(regSeqNo.get(0));
            int nextSeq = currentSequence + 1;
            config.setConfiguration(Collections.singletonList(Integer.toString(nextSeq)));
            mongoTemplate.save(config);
            return String.valueOf(currentSequence);
        }else {
            return null;
        }
    }
}
