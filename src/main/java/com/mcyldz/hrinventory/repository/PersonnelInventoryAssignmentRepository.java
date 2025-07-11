package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.PersonnelInventoryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonnelInventoryAssignmentRepository extends JpaRepository<PersonnelInventoryAssignment, UUID> {
}
