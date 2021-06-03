package com.onlineclass.service.impl;

import com.onlineclass.entity.CollectionContentRelation;
import com.onlineclass.exception.ServiceException;
import com.onlineclass.repository.CollectionContentRelationRepository;
import com.onlineclass.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CollectionContentServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CollectionContentRelationRepository collectionContentRelationRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Transactional
    public void update(Integer collectionId, Set<Integer> contentIds) throws ServiceException {
        if(contentIds == null || contentIds.isEmpty()){
            log.info("content id list is empty, nothing to update");
            return;
        }
        log.info("deleting all relations with collection id : {}", collectionId);
        collectionContentRelationRepository.deleteByCollectionId(collectionId);

        Set<Integer> existingContentIds = contentRepository.findByIdIn(contentIds);
        log.info("fetched existing content ids : {}", existingContentIds);

        Set<Integer> nonExistingContentIds = contentIds.stream().filter(c -> !existingContentIds.contains(c)).collect(Collectors.toSet());
        if(nonExistingContentIds.size() > 0){
            log.error("non existing content ids are passed : {}", nonExistingContentIds);
            throw new ServiceException("non existing content ids are passed", 404);
        }

        Set<CollectionContentRelation> entities = contentIds.stream().map(c -> new CollectionContentRelation(collectionId, c)).collect(Collectors.toSet());
        log.info("saving collection content relations in db : {}", entities);
        collectionContentRelationRepository.saveAll(entities);
    }

    @Transactional
    public void deleteByCollectionId(Integer collectionId) {
        collectionContentRelationRepository.deleteByCollectionId(collectionId);
        log.info("deleted all relations with collection id : {}", collectionId);
    }

    @Transactional
    public void deleteByContentId(Integer contentId) {
        collectionContentRelationRepository.deleteByContentId(contentId);
        log.info("deleted all relations with content id : {}", contentId);
    }

}
