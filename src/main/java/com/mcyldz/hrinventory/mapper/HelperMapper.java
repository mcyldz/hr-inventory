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
        return "Brand: " + inventoryItem.getBrand() + " Model:" + inventoryItem.getModel() + " Serial Number: " + inventoryItem.getSerialNumber();
    }
}
