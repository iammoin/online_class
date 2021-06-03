package com.onlineclass.service.impl;

import com.onlineclass.dto.UserType;
import com.onlineclass.entity.Collection;
import com.onlineclass.exception.ServiceException;
import com.onlineclass.repository.CollectionRepository;
import com.onlineclass.request.BaseRequest;
import com.onlineclass.request.CollectionBody;
import com.onlineclass.response.BaseResponse;
import com.onlineclass.validator.CollectionCreatorValidator;
import com.onlineclass.validator.PrimaryIdValidator;
import com.onlineclass.validator.UserTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;

@Service
public class CollectionServiceImpl extends AbstractService{
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private PrimaryIdValidator primaryIdValidator;

    @Autowired
    private UserTypeValidator userTypeValidator;

    @Autowired
    private ContentServiceImpl contentService;

    @Autowired
    private CollectionContentServiceImpl collectionContentService;

    @Autowired
    private CollectionCreatorValidator collectionCreatorValidator;

    @Transactional
    public BaseResponse create(BaseRequest<CollectionBody> request){
        CollectionBody body = request.getBody();
        try{
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            collectionCreatorValidator.validate(body);
            Collection collection = create(body);
            Set<Integer> contentIds = body.getContentIds();
            collectionContentService.update(collection.getId(), contentIds);
            body.setCollectionId(collection.getId());
        } catch (ServiceException ex){
            log.error("service exception occurred while creating collection ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while creating collection ", ex);
            return handleUnknownException();
        }
        return handleSuccess(body);
    }

    @Transactional
    public BaseResponse update(BaseRequest<CollectionBody> request) {
        CollectionBody body = request.getBody();
        try{
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            Collection collection = update(body);
            Set<Integer> contentIds = body.getContentIds();
            collectionContentService.update(collection.getId(), contentIds);
            body.update(collection);
        } catch (ServiceException ex){
            log.error("service exception occurred while updating collection ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while updating collection ", ex);
            return handleUnknownException();
        }
        return handleSuccess(body);
    }

    public BaseResponse delete(BaseRequest request, Integer collectionId){
        try {
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            collectionContentService.deleteByCollectionId(collectionId);
            delete(collectionId);
        } catch (EmptyResultDataAccessException ex){
            log.error("collection entity does not exist with collectionId : " + collectionId, ex);
            return handleRuntimeException("collectionId does not exist", 404);
        }catch (ServiceException ex){
            log.error("service exception occurred while deleting collection ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while deleting collection ", ex);
            return handleUnknownException();
        }
        return handleSuccess(null);
    }

    public Collection create(CollectionBody body){
        Collection collection = collectionRepository.save(new Collection(body));
        log.info("saved collection in DB : {}", collection);
        return collection;
    }

    public Collection update(CollectionBody body) throws ServiceException {
        if(!primaryIdValidator.validate(body.getCollectionId()))
            throw new ServiceException("invalid collection id", 400);
        Optional<Collection> optionalContent = collectionRepository.findById(body.getCollectionId());
        if(!optionalContent.isPresent())
            throw new ServiceException("collection does not exist for collection id " + body.getCollectionId(), 404);
        Collection collection = optionalContent.get();
        collection.updateNotNullProperties(body);
        collection = collectionRepository.save(collection);
        log.info("updated collection in DB : {}", collection);
        return collection;
    }

    public void delete(Integer collectionId) throws ServiceException {
        if(!primaryIdValidator.validate(collectionId))
            throw new ServiceException("invalid collection id", 400);
        collectionRepository.deleteById(collectionId);
        log.info("deleted collection id :{} successfully", collectionId);
    }
}
