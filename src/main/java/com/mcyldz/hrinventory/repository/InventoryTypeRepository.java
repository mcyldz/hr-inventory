package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.InventoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryTypeRepository extends JpaRepository<InventoryType, UUID> {

    Optional<InventoryType> findByName(String name);
}
