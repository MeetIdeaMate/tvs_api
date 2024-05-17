package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Config;
import com.techlambdas.delearmanagementapp.request.ConfigReq;
import com.techlambdas.delearmanagementapp.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @PostMapping
    public ResponseEntity feedConfigData(@RequestBody ConfigReq configReq){
        Config savedConfig= configService.save(configReq);
        return new ResponseEntity(savedConfig,HttpStatus.CREATED);
    }

    @GetMapping("/{configId}")
    public ResponseEntity getConfigurationByConfigId(@PathVariable String configId){
    Config config=configService.findConfigByConfigId(configId);
    return ResponseEntity.ok(config);
    }
    @PutMapping("/edit/{configId}")
    public ResponseEntity updateConfigurationTable(@PathVariable String configId, @RequestBody ConfigReq configReq){
        Config savedConfig= configService.updateConfig(configId,configReq);
        return new ResponseEntity(savedConfig,HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Config>> getAllConfiguration() {
        List<Config> config = configService.findAll();
        return new ResponseEntity<>(config, HttpStatus.OK);
    }
    @PatchMapping("/push/{configId}")
    public ResponseEntity<String> addConfigurationTable(@PathVariable String configId, @RequestParam String configValue ) {
        Config existingConfig = configService.addConfigValue(configId, configValue);
        return ResponseEntity.ok("Added values in table Successfully");
        }
    @PatchMapping("/pop/{configId}")
    public ResponseEntity<String> deleteConfigurationTableValue(@PathVariable String configId, @RequestParam String configValue){
     Config existingConfig = configService.removeConfigValue(configId,configValue);
     return ResponseEntity.ok("Removed Values From Table Successfully");
    }

}
