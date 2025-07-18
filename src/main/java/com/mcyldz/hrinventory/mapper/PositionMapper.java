package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.PositionCreateRequest;
import com.mcyldz.hrinventory.dto.request.PositionUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PositionResponse;
import com.mcyldz.hrinventory.entity.Position;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    Position toPosition(PositionCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePositionFromRequest(PositionUpdateRequest request, @MappingTarget Position position);

    PositionResponse toPositionResponse(Position position);

    List<PositionResponse> toPositionResponseList(List<Position> positionList);
}
