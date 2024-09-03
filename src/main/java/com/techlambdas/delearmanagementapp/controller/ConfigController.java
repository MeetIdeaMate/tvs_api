package com.techlambdas.delearmanagementapp.controller;
import com.techlambdas.delearmanagementapp.model.Config;
import com.techlambdas.delearmanagementapp.request.ConfigReq;
import com.techlambdas.delearmanagementapp.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @PostMapping
    public ResponseEntity feedConfigData(@RequestBody ConfigReq configReq){
        Config savedConfig= configService.save(configReq);
        return successResponse(HttpStatus.CREATED,"config",savedConfig);
    }

    @GetMapping("/{configId}")
    public ResponseEntity getConfigurationByConfigId(@PathVariable String configId){
        Config config=configService.findConfigByConfigId(configId);
        return successResponse(HttpStatus.OK,"config",config);

    }
    @PutMapping("/edit/{configId}")
    public ResponseEntity updateConfigurationTable(@PathVariable String configId, @RequestBody ConfigReq configReq){
        Config savedConfig= configService.updateConfig(configId,configReq);
        return successResponse(HttpStatus.OK,"config",savedConfig);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Config>> getAllConfiguration(@RequestParam (required = false)String configId) {
        List<Config> config = configService.findAll(configId);
        return successResponse(HttpStatus.OK,"configList",config);
    }
    @PatchMapping("/push/{configId}")
    public ResponseEntity<Config> addConfigurationTable(@PathVariable String configId, @RequestParam String configValue ) {
        Config existingConfig = configService.addConfigValue(configId, configValue);
        return successResponse(HttpStatus.OK,"config",existingConfig);
    }
    @PatchMapping("/pop/{configId}")
    public ResponseEntity<Config> deleteConfigurationTableValue(@PathVariable String configId, @RequestParam String configValue){
        Config existingConfig = configService.removeConfigValue(configId,configValue);
        return successResponse(HttpStatus.OK,"config",existingConfig);
    }

}
