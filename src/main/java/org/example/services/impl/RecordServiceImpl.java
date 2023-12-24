package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.requests.CreateUpdateRecordRequest;
import org.example.dao.responses.RecordResponse;
import org.example.dao.responses.ShortRecordResponse;
import org.example.exceptions.RecordNotFoundException;
import org.example.models.Record;
import org.example.repositories.RecordRepository;
import org.example.services.RecordService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    @Override
    public RecordResponse saveRecord(Integer userId, CreateUpdateRecordRequest createUpdateRecordRequest) {
        if (createUpdateRecordRequest.getName() == null || createUpdateRecordRequest.getPassword() == null) {
            throw new IllegalArgumentException("Name and Password are necessary");
        }
        Record record = new Record(UUID.randomUUID(),
                createUpdateRecordRequest.getName(),
                createUpdateRecordRequest.getLogin(),
                createUpdateRecordRequest.getPassword(),
                createUpdateRecordRequest.getUrl(),
                userId);
        recordRepository.save(record);
        return new RecordResponse(record.getId(), record.getName(), record.getLogin(), record.getPassword(), record.getUrl());
    }

    @Override
    public List<ShortRecordResponse> getRecords(Integer userId) {
        List<Record> records = recordRepository.getByUserId(userId);
        List<ShortRecordResponse> response = new ArrayList<>();
        for (var record: records) {
            response.add(new ShortRecordResponse(record.getId(), record.getName()));
        }
        return response;
    }

    @Override
    public RecordResponse getRecord(UUID id, Integer userId) throws RecordNotFoundException {
        Record record = recordRepository.getByIdAndUserId(id, userId);
        if (record == null) {
            throw new RecordNotFoundException("No record with such id");
        }
        return new RecordResponse(record.getId(), record.getName(), record.getLogin(), record.getPassword(), record.getUrl());
    }

    @Override
    public RecordResponse updateRecord(UUID id, Integer userId, CreateUpdateRecordRequest updateRecordRequest) throws RecordNotFoundException {
        Record record = recordRepository.getByIdAndUserId(id, userId);
        if (record == null) {
            throw new RecordNotFoundException("No record with such id");
        }
        record.setName(updateRecordRequest.getName() == null ? record.getName() : updateRecordRequest.getName());
        record.setLogin(updateRecordRequest.getLogin() == null ? record.getLogin() : updateRecordRequest.getLogin());
        record.setPassword(updateRecordRequest.getPassword() == null ? record.getPassword() : updateRecordRequest.getPassword());
        record.setUrl(updateRecordRequest.getUrl() == null ? record.getUrl() : updateRecordRequest.getUrl());
        recordRepository.save(record);
        return new RecordResponse(record.getId(), record.getName(), record.getLogin(), record.getPassword(), record.getUrl());
    }

    @Override
    public List<ShortRecordResponse> getRecords(Integer userId, Integer pageSize, Integer pageNum) {
        Pageable page = PageRequest.of(pageNum - 1, pageSize);
        List<Record> records = recordRepository.findAllByUserId(userId, page);
        List<ShortRecordResponse> response = new ArrayList<>();
        for (var record: records) {
            response.add(new ShortRecordResponse(record.getId(), record.getName()));
        }
        return response;
    }
}
