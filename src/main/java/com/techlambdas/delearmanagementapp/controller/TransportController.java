package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Transport;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.request.TransportRequest;
import com.techlambdas.delearmanagementapp.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/transport")
public class TransportController {
    @Autowired
    private TransportService transportService;
    @PostMapping
    public ResponseEntity<Transport> createTransport(@RequestBody TransportRequest transportRequest) {
        Transport transport = transportService.createTransport(transportRequest);
        return successResponse(HttpStatus.CREATED,"transport",transport);
    }
    @GetMapping
    public ResponseEntity<List<Transport>>getAllTransports(@RequestParam(required = false) String transportId,
                                                     @RequestParam(required = false) String transportName,
                                                     @RequestParam(required = false) String mobileNo,
                                                     @RequestParam(required = false) String city){
        List<Transport> transports=transportService.getAllTransports(transportId,transportName,mobileNo,city);
        return successResponse(HttpStatus.OK,"transports",transports);
    }
    @PutMapping("/{transportId}")
    public ResponseEntity<Transport> updateTransport(@PathVariable String transportId,@RequestBody TransportRequest transportRequest){
        Transport transport=transportService.updateTransport(transportId,transportRequest);
        return new ResponseEntity<>(transport, HttpStatus.OK);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Transport>> getAllTransportWithPage(
            @RequestParam(required = false) String transportId,
            @RequestParam(required = false) String transportName,
            @RequestParam(required = false) String mobileNo,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Transport> transportsPage = transportService.getAllTransportsWithPage(transportId, transportName, mobileNo, city, pageable);

        return successResponse(HttpStatus.OK,"transportsWithPage",transportsPage);
    }
    @GetMapping("/{transportId}")
    public ResponseEntity<Transport> getTransportById(@PathVariable String transportId){
        Transport transport=transportService.getTransportByTransportId(transportId);
        return successResponse(HttpStatus.OK,"transport",transport);

    }
}
