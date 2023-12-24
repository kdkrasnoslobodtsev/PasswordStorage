package org.example.services;

import org.example.dao.responses.RecordResponse;
import org.example.exceptions.SharingRecordNotFoundException;
import org.example.exceptions.SharingRecordTimeExpiredException;
import org.example.models.Record;
import org.example.models.SharingRecord;

import javax.management.InstanceNotFoundException;

public interface SharingRecordService {
    SharingRecord getSharingRecord(String token) throws SharingRecordNotFoundException, SharingRecordTimeExpiredException;

    String saveSharingRecord(Record record);
}