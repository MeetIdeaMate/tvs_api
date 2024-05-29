package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Transport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTransportRepository {
    List<Transport> getAllTransports(String transportId, String transportName, String mobileNo, String city);
    Page<Transport> getAllTransportsWithPage(String transportId, String transportName, String mobileNo, String city, Pageable pageable);
}
