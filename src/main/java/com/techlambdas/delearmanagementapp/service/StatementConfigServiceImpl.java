package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.StatementConfigMapper;
import com.techlambdas.delearmanagementapp.model.StatementConfig;
import com.techlambdas.delearmanagementapp.repository.StatementConfigRepository;
import com.techlambdas.delearmanagementapp.request.StatementConfigReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementConfigServiceImpl implements StatementConfigService{

    @Autowired
    private StatementConfigRepository statementConfigRepository;

    @Autowired
    private StatementConfigMapper statementConfigMapper;

    @Override
    public StatementConfig createStatementConfig(StatementConfigReq statementConfigReq) {

   StatementConfig exsistStatementConfig = statementConfigRepository.findByStatementConfigId(statementConfigReq.getStatementConfigId());

           if(exsistStatementConfig!=null)
               throw new AlreadyExistException("StatementConfigId Already Exist with ID: "+ exsistStatementConfig.getStatementConfigId());
          return statementConfigRepository.save(statementConfigMapper.mapReqToEntity(statementConfigReq));
   }

    @Override
    public StatementConfig updateStatementConfig(StatementConfigReq statementConfigReq, String statementConfigId) {

            StatementConfig exsistStatementConfig = statementConfigRepository.findByStatementConfigId(statementConfigId);

            if(exsistStatementConfig==null)
                throw new DataNotFoundException("StatementConfig Not Found With Id: "+statementConfigId);
            return  statementConfigRepository.save(statementConfigMapper.updateReqToEntity(statementConfigReq,exsistStatementConfig));

         }

    @Override
    public List<StatementConfig> getAllConfig() {
        return statementConfigRepository.findAll();
    }

    @Override
    public StatementConfig getByStatementConfigId(String statementConfigId) {
        StatementConfig exsistStatementConfig = statementConfigRepository.findByStatementConfigId(statementConfigId);

        if(exsistStatementConfig==null)
            throw new DataNotFoundException("StatementConfig Not Found With Id: "+statementConfigId);
        return exsistStatementConfig;
    }

    @Override
    public void deleteStatementConfig(String statementConfigId) {
        StatementConfig exsistStatementConfig = statementConfigRepository.findByStatementConfigId(statementConfigId);
        if(exsistStatementConfig==null)
            throw new DataNotFoundException("StatementConfig Not Found With Id: "+statementConfigId);
        statementConfigRepository.delete(exsistStatementConfig);
    }
}
