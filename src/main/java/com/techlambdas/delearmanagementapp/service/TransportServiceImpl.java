package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.TransportMapper;
import com.techlambdas.delearmanagementapp.model.Transport;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.repository.CustomTransportRepository;
import com.techlambdas.delearmanagementapp.repository.TransportRepository;
import com.techlambdas.delearmanagementapp.request.TransportRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportServiceImpl implements TransportService{
    @Autowired
    private TransportMapper transportMapper;
    @Autowired
    private TransportRepository transportRepository;
    @Autowired
    private CustomTransportRepository customTransportRepository;
    @Override
    public Transport createTransport(TransportRequest transportRequest) {
        try {
            Transport transport=transportMapper.mapTransportRequestToTransport(transportRequest);
            transport.setTransportId(RandomIdGenerator.getRandomId());
            return transportRepository.save(transport);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Transport> getAllTransports(String transportId, String transportName, String mobileNo, String city) {
        List<Transport> transports=customTransportRepository.getAllTransports(transportId,transportName,mobileNo,city);
        return transports;
    }

    @Override
    public Transport updateTransport(String transportId, TransportRequest transportRequest) {
        try {
            Transport existingTransport = transportRepository.findBytransportId(transportId);
            if (existingTransport == null)
                throw new DataNotFoundException("transport not found with ID: " + transportId);
            transportMapper.updateVendorFromRequest(transportRequest, existingTransport);
            return transportRepository.save(existingTransport);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<Transport> getAllTransportsWithPage(String transportId, String transportName, String mobileNo, String city, Pageable pageable) {
        return customTransportRepository.getAllTransportsWithPage(transportId,transportName,mobileNo,city,  pageable);
    }
}
