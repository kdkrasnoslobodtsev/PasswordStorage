package org.example.repositories;

import jakarta.transaction.Transactional;
import org.example.models.SharingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SharingRecordRepository extends JpaRepository<SharingRecord, UUID> {
    SharingRecord getByToken(String token);
}