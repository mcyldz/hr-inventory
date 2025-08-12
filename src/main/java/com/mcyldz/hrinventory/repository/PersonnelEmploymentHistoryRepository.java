package com.mcyldz.hrinventory.repository;

import com.mcyldz.hrinventory.entity.PersonnelEmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonnelEmploymentHistoryRepository extends JpaRepository<PersonnelEmploymentHistory, UUID> {

    List<PersonnelEmploymentHistory> findByPersonnelId(UUID personnelId);

    Optional<PersonnelEmploymentHistory> findByPersonnelIdAndEndDateIsNull(UUID personnelId);
}
