package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.dto.request.PersonnelCreateRequest;
import com.mcyldz.hrinventory.dto.request.PersonnelUpdateRequest;
import com.mcyldz.hrinventory.dto.response.PersonnelResponse;
import com.mcyldz.hrinventory.entity.Personnel;
import org.mapstruct.*;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonnelMapper {

    @Mapping(target = "educationLevel", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "profilePhoto", source = "profilePhoto", qualifiedByName = "base64ToByteArray")
    Personnel toPersonnel(PersonnelCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "educationLevel", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(source = "profilePhoto", target = "profilePhoto", qualifiedByName = "base64ToByteArray")
    void updatePersonnelFromRequest(PersonnelUpdateRequest request, @MappingTarget Personnel personnel);

    @Mapping(source = "educationLevel.name", target = "educationLevelName")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.name", target = "positionName")
    @Mapping(source = "profilePhoto", target = "profilePhoto", qualifiedByName = "byteArrayToBase64")
    PersonnelResponse toPersonnelResponse(Personnel personnel);

    List<PersonnelResponse> toPersonnelResponseList(List<Personnel> personnelList);

    @Named("base64ToByteArray")
    default byte[] base64ToByteArray(String base64){
        if (base64 == null || base64.isEmpty()){
            return null;
        }
        if (base64.contains(",")){
            base64 = base64.split(",")[1];
        }
        return Base64.getDecoder().decode(base64);
    }

    @Named("byteArrayToBase64")
    default String byteArrayToBase64(byte[] bytes){
        if (bytes == null || bytes.length == 0){
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
