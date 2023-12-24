package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.SharingRecordNotFoundException;
import org.example.exceptions.SharingRecordTimeExpiredException;
import org.example.models.Record;
import org.example.models.SharingRecord;
import org.example.repositories.SharingRecordRepository;
import org.example.services.SharingRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharingRecordServiceImpl implements SharingRecordService {
    private final SharingRecordRepository sharingRecordRepository;

    private static String characters;
    private static char[] token;
    private static long interval;

    static {
        characters = "1234567890qwertyuiopasdfghjklzxcvbnm";
        token = new char[30];
        interval = 1800000;
    }

    @Override
    public SharingRecord getSharingRecord(String token) throws SharingRecordNotFoundException, SharingRecordTimeExpiredException {
        SharingRecord found = sharingRecordRepository.getByToken(token);
        if (found == null) {
            throw new SharingRecordNotFoundException("Cannot find a record with token " + token);
        }
        if (new Date().getTime() - found.getCreationDate().getTime() > interval) {
            throw new SharingRecordTimeExpiredException("Token is unavailable now");
        }
        sharingRecordRepository.delete(found);
        return found;
    }

    @Override
    public String saveSharingRecord(Record record) {
        for (int i = 0; i < 30; i++) {
            token[i] = characters.charAt(new Random().nextInt(characters.length()));
        }
        SharingRecord newRecord = new SharingRecord(UUID.randomUUID(), record.getName(), record.getLogin(), record.getPassword(), new String(token), new Date());
        sharingRecordRepository.save(newRecord);
        return newRecord.getToken();
    }
}