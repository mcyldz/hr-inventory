package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, UUID> {

    Optional<Personnel> findByTcIdentityNumber(String tcIdentityNumber);
}
