package com.techlambdas.delearmanagementapp.response;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class AppResponse {

    private  Map<String,Object> response;
    private  ResponseEntity buildResponse(String status,HttpStatus httpStatus,Object result, Object error){
        response = new HashMap<>();
        response.put("status", httpStatus);
        response.put("result", Optional.of(result));
        response.put("error", Optional.of(error));
        response.put("statusCode",httpStatus.value());
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static ResponseEntity successResponse(HttpStatus status, String responseName, Object result){
        return new AppResponse().buildResponse("success",status,ImmutableMap.of(responseName,result),Optional.empty());
    }

    public static ResponseEntity errorResponse(HttpStatus status, Object error){
        return new AppResponse().buildResponse("error",status,Optional.empty(),error);
    }

}
