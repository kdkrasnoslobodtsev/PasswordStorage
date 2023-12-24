package org.example.repositories;

import org.example.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, UUID> {
    List<Record> getByUserId(Integer userId);

    Record getByIdAndUserId(UUID id, Integer userId);

    List<Record> findAllByUserId(Integer userId, Pageable page);
}
