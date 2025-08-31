package com.mcyldz.hrinventory.mapper;

import com.mcyldz.hrinventory.entity.InventoryItem;
import com.mcyldz.hrinventory.entity.Personnel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HelperMapper {

    default String personnelToFullName(Personnel personnel){
        if (personnel == null){
            return null;
        }
        return personnel.getFirstName() + " " + personnel.getLastName();
    }

    default String inventoryItemToDescription(InventoryItem inventoryItem){
        if (inventoryItem == null){
            return null;
        }
        return inventoryItem.getBrand() + " " + inventoryItem.getModel() + " - " + inventoryItem.getSerialNumber();
    }
}
