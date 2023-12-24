package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dao.requests.CreateUpdateRecordRequest;
import org.example.dao.responses.RecordResponse;
import org.example.exceptions.RecordNotFoundException;
import org.example.exceptions.SharingRecordNotFoundException;
import org.example.exceptions.SharingRecordTimeExpiredException;
import org.example.models.Record;
import org.example.models.SharingRecord;
import org.example.models.User;
import org.example.services.RecordService;
import org.example.services.SharingRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/record/share")
@RequiredArgsConstructor
public class SharingRecordController {
    private final SharingRecordService sharingRecordService;
    private final RecordService recordService;

    @PostMapping("{id}")
    public ResponseEntity<String> createSharingRecord(@PathVariable UUID id) throws RecordNotFoundException {
        RecordResponse record;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        record = recordService.getRecord(id, ((User)authentication.getPrincipal()).getId());
        String response = sharingRecordService.saveSharingRecord(new Record(record.getId(), record.getName(), record.getLogin(), record.getPassword(), record.getUrl(), ((User)authentication.getPrincipal()).getId()));
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> getSharingRecord(@RequestParam String shareToken) throws SharingRecordNotFoundException, SharingRecordTimeExpiredException {
        SharingRecord sharingRecord;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        sharingRecord = sharingRecordService.getSharingRecord(shareToken);
        RecordResponse response = recordService.saveRecord(((User)authentication.getPrincipal()).getId(), new CreateUpdateRecordRequest(sharingRecord.getName(), sharingRecord.getLogin(), sharingRecord.getPassword(), ""));
        return new ResponseEntity<UUID>(response.getId(), HttpStatus.OK);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleRecordException(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SharingRecordNotFoundException.class)
    public ResponseEntity<String> handleSharingRecordException(SharingRecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SharingRecordTimeExpiredException.class)
    public ResponseEntity<String> handleSharingRecordExpiredException(SharingRecordTimeExpiredException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}