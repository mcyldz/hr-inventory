package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryStatusRepository extends JpaRepository<InventoryStatus, UUID> {
}
