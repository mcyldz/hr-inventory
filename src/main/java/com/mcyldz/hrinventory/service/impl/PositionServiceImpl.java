package com.mcyldz.hrinventory.service.impl;

import com.mcyldz.hrinventory.dto.request.PositionCreateRequest;
import com.mcyldz.hrinventory.dto.request.PositionUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PositionResponse;
import com.mcyldz.hrinventory.entity.Position;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.exception.model.ResourceNotFoundException;
import com.mcyldz.hrinventory.mapper.PositionMapper;
import com.mcyldz.hrinventory.repository.PositionRepository;
import com.mcyldz.hrinventory.service.PositionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponse getPositionById(UUID id) {
        return positionMapper.toPositionResponse(findPositionByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponse> getAllPositions(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Position> positionPage = positionRepository.findAll(pageable);

        List<Position> positionList = positionPage.getContent();

        return positionMapper.toPositionResponseList(positionList);
    }

    @Override
    @Transactional
    public PositionResponse createPosition(PositionCreateRequest request) {
        Position position = positionMapper.toPosition(request);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toPositionResponse(savedPosition);
    }

    @Override
    @Transactional
    public PositionResponse updatePosition(UUID id, PositionUpdateRequest request) {
        Position existingPosition = findPositionByIdOrThrow(id);
        positionMapper.updatePositionFromRequest(request, existingPosition);
        Position updatedPosition = positionRepository.save(existingPosition);
        return positionMapper.toPositionResponse(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(UUID id) {
        if (!positionRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.POSITION_NOT_FOUND);
        }
        positionRepository.deleteById(id);
    }

    private Position findPositionByIdOrThrow(UUID id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.POSITION_NOT_FOUND));
    }
}
