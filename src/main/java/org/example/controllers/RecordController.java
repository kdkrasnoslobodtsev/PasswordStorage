package org.example.controllers;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.example.dao.requests.CreateUpdateRecordRequest;
import org.example.dao.responses.RecordResponse;
import org.example.dao.responses.ShortRecordResponse;
import org.example.exceptions.RecordNotFoundException;
import org.example.models.User;
import org.example.services.JwtService;
import org.example.services.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody CreateUpdateRecordRequest createUpdateRecordRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            RecordResponse record = recordService.saveRecord(((User)authentication.getPrincipal()).getId(), createUpdateRecordRequest);
            return new ResponseEntity<RecordResponse>(record, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ShortRecordResponse>> getAllRecords(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (pageSize == null || pageNum == null) {
            return new ResponseEntity<>(recordService.getRecords(((User) authentication.getPrincipal()).getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recordService.getRecords(((User)authentication.getPrincipal()).getId(), pageSize, pageNum), HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getRecord(@PathVariable UUID id) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RecordResponse record = recordService.getRecord(id, ((User)authentication.getPrincipal()).getId());
        return new ResponseEntity<RecordResponse>(record, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateRecord(@PathVariable UUID id, @RequestBody CreateUpdateRecordRequest createUpdateRecordRequest) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RecordResponse record = recordService.updateRecord(id, ((User)authentication.getPrincipal()).getId(), createUpdateRecordRequest);
        return new ResponseEntity<RecordResponse>(record, HttpStatus.OK);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleException(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
