package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.InventoryStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryStatusHistoryRepository extends JpaRepository<InventoryStatusHistory, UUID> {

    List<InventoryStatusHistory> findByInventoryItemId(UUID inventoryItemId);
}
