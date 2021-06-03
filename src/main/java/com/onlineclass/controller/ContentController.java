package com.onlineclass.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineclass.request.BaseRequest;
import com.onlineclass.request.ContentBody;
import com.onlineclass.request.SearchBody;
import com.onlineclass.response.BaseResponse;
import com.onlineclass.service.impl.ContentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
public class ContentController {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ContentServiceImpl contentService;

    @PostMapping(value = {"/content/create"})
    public ResponseEntity create(@RequestBody BaseRequest<ContentBody> request) throws JsonProcessingException {
        log.info("content create request : {} ", request);
        BaseResponse response = contentService.create(request);
        log.info("content create response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

    @PostMapping(value = {"/content/update"})
    public ResponseEntity update(@RequestBody BaseRequest<ContentBody> request) throws JsonProcessingException {
        log.info("content update request : {} ", request);
        BaseResponse response = contentService.update(request);
        log.info("content update response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

    @DeleteMapping(value = {"/content/delete/{contentId}"})
    public ResponseEntity delete(@RequestBody BaseRequest request, @PathVariable Integer contentId) throws JsonProcessingException {
        log.info("content delete request : {}, content id : {} ", request, contentId);
        BaseResponse response = contentService.delete(request, contentId);
        log.info("content delete response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

    @PostMapping(value = {"/content/search"})
    public ResponseEntity delete(@RequestBody BaseRequest<SearchBody> request) throws JsonProcessingException {
        log.info("content search request : {}", request);
        BaseResponse response = contentService.search(request);
        log.info("content search response : {}", response);
        return ResponseEntity.status(HttpStatus.valueOf(response.getMessageCode()))
                .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response));
    }

}
