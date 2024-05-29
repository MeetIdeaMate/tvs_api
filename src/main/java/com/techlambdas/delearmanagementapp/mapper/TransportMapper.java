package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Transport;
import com.techlambdas.delearmanagementapp.request.TransportRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransportMapper {
    Transport mapTransportRequestToTransport(TransportRequest transportRequest);

    void updateVendorFromRequest(TransportRequest transportRequest,@MappingTarget Transport existingTransport);
}
