package com.onlineclass.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineclass.request.BaseRequest;
import com.onlineclass.request.CollectionBody;
import com.onlineclass.request.ContentBody;
import com.onlineclass.response.BaseResponse;
import com.onlineclass.service.impl.CollectionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
public class CollectionController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CollectionServiceImpl collectionService;

    @PostMapping(value = {"/collection/create"})
    public ResponseEntity create(@RequestBody BaseRequest<CollectionBody> request) throws JsonProcessingException {
        log.info("collection create request : {} ", request);
        BaseResponse response = collectionService.create(request);
        log.info("collection create response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

    @PostMapping(value = {"/collection/update"})
    public ResponseEntity update(@RequestBody BaseRequest<CollectionBody> request) throws JsonProcessingException {
        log.info("collection update request : {} ", request);
        BaseResponse response = collectionService.update(request);
        log.info("collection update response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

    @DeleteMapping(value = {"/collection/delete/{collectionId}"})
    public ResponseEntity delete(@RequestBody BaseRequest request, @PathVariable Integer collectionId) throws JsonProcessingException {
        log.info("collection delete request : {}, collection id : {} ", request, collectionId);
        BaseResponse response = collectionService.delete(request, collectionId);
        log.info("collection delete response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }
}
