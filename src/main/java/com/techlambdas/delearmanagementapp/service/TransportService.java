package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Transport;
import com.techlambdas.delearmanagementapp.request.TransportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransportService {
    Transport createTransport(TransportRequest transportRequest);
    List<Transport> getAllTransports(String transportId, String transportName, String mobileNo, String city);
    Transport updateTransport(String transportId, TransportRequest transportRequest);
    Page<Transport> getAllTransportsWithPage(String transportId, String transportName, String mobileNo, String city, Pageable pageable);
}
