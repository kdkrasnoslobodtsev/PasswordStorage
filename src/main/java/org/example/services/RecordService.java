package org.example.services;

import org.example.dao.requests.CreateUpdateRecordRequest;
import org.example.dao.responses.RecordResponse;
import org.example.dao.responses.ShortRecordResponse;
import org.example.exceptions.RecordNotFoundException;

import java.util.List;
import java.util.UUID;

public interface RecordService {
    RecordResponse saveRecord(Integer userId, CreateUpdateRecordRequest createRecordRequest);
    List<ShortRecordResponse> getRecords(Integer userId);
    RecordResponse getRecord(UUID id, Integer userId) throws RecordNotFoundException;
    RecordResponse updateRecord(UUID id, Integer userId, CreateUpdateRecordRequest updateRecordRequest) throws RecordNotFoundException;
    List<ShortRecordResponse> getRecords(Integer userId, Integer pageSize, Integer pageNum);
}
