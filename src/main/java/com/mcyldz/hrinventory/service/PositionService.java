package com.mcyldz.hrinventory.service;

import com.mcyldz.hrinventory.dto.request.PositionCreateRequest;
import com.mcyldz.hrinventory.dto.request.PositionUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PositionResponse;

import java.util.List;
import java.util.UUID;

public interface PositionService {

    PositionResponse getPositionById(UUID id);

    List<PositionResponse> getAllPositions(Integer page, Integer size);

    PositionResponse createPosition(PositionCreateRequest request);

    PositionResponse updatePosition(UUID id, PositionUpdateRequest request);

    void deletePosition(UUID id);
}
