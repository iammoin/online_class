package com.onlineclass.service.impl;

import com.onlineclass.dto.CollectionType;
import com.onlineclass.dto.ContentType;
import com.onlineclass.dto.UserType;
import com.onlineclass.entity.Collection;
import com.onlineclass.entity.CollectionContentRelation;
import com.onlineclass.entity.Content;
import com.onlineclass.exception.ServiceException;
import com.onlineclass.repository.ContentRepository;
import com.onlineclass.request.BaseRequest;
import com.onlineclass.request.CollectionBody;
import com.onlineclass.request.ContentBody;
import com.onlineclass.request.SearchBody;
import com.onlineclass.response.BaseResponse;
import com.onlineclass.response.SearchResponseBody;
import com.onlineclass.validator.ContentCreatorValidator;
import com.onlineclass.validator.PrimaryIdValidator;
import com.onlineclass.validator.UserTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class ContentServiceImpl extends AbstractService{
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private PrimaryIdValidator primaryIdValidator;

    @Autowired
    private UserTypeValidator userTypeValidator;

    @Autowired
    private ContentCreatorValidator contentCreatorValidator;

    @Autowired
    private CollectionServiceImpl collectionService;

    @Autowired
    private CollectionContentServiceImpl collectionContentService;

    @Value("${content.search.page.number}")
    private Integer defaultPageNumber;

    @Value("${content.search.page.size}")
    private Integer defaultPageSize;

    @Autowired
    private EntityManager entityManager;

    public BaseResponse create(BaseRequest<ContentBody> request){
        ContentBody body = request.getBody();
        try{
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            contentCreatorValidator.validate(body);
            Content content = create(body);
            CollectionBody collectionBody = body.getCollectionBody();

            if((ContentType.VIDEO.equals(body.getContentType()) || ContentType.WEBINAR.equals(body.getContentType()))
                    && collectionBody != null && CollectionType.TAG.equals(collectionBody.getCollectionType())){
                log.info("creating a tag while uploading a video or a webinar");
                Collection collection = collectionService.create(collectionBody);
                Set<Integer> contentIds = new HashSet<>();
                contentIds.add(content.getId());
                collectionContentService.update(collection.getId(), contentIds);
                collectionBody.setCollectionId(collection.getId());
            } else {
                body.setCollectionBody(null);
            }

            body.setContentId(content.getId());
        } catch (ServiceException ex){
            log.error("service exception occurred while creating content ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while creating content ", ex);
            return handleUnknownException();
        }
        return handleSuccess(body);
    }

    public BaseResponse update(BaseRequest<ContentBody> request) {
        ContentBody body = request.getBody();
        try{
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            Content content = update(body);
            body.update(content);
        } catch (ServiceException ex){
            log.error("service exception occurred while updating content ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while updating content ", ex);
            return handleUnknownException();
        }
        return handleSuccess(body);
    }

    public BaseResponse delete(BaseRequest request, Integer contentId){
        try {
            userTypeValidator.validate(request.getUserType(), UserType.INSTRUCTOR);
            collectionContentService.deleteByContentId(contentId);
            delete(contentId);
        } catch (EmptyResultDataAccessException ex){
            log.error("content entity does not exist with contentId : " + contentId, ex);
            return handleRuntimeException("contentId does not exist", 404);
        }catch (ServiceException ex){
            log.error("service exception occurred while deleting content ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while deleting content ", ex);
            return handleUnknownException();
        }
        return handleSuccess(null);
    }

    public Content create(ContentBody body){
        Content content = contentRepository.save(new Content(body));
        log.info("saved content in DB : {}", content);
        return content;
    }

    public Content update(ContentBody body) throws ServiceException {
        if(!primaryIdValidator.validate(body.getContentId()))
            throw new ServiceException(INVALID_CONTENT_ID, 400);
        Optional<Content> optionalContent = contentRepository.findById(body.getContentId());
        if(!optionalContent.isPresent())
            throw new ServiceException(CONTENT_DOES_NOT_EXIST_FOR_CONTENT_ID + body.getContentId(), 404);
        Content content = optionalContent.get();
        content.updateNotNullProperties(body);
        content = contentRepository.save(content);
        log.info("updated content in DB : {}", content);
        return content;
    }

    public void delete(Integer contentId) throws ServiceException {
        if(!primaryIdValidator.validate(contentId))
            throw new ServiceException("invalid content id", 400);
        contentRepository.deleteById(contentId);
        log.info("deleted content id :{} successfully", contentId);
    }

    public BaseResponse search(BaseRequest<SearchBody> request){
        SearchBody body = request.getBody();
        SearchResponseBody searchResponseBody = new SearchResponseBody();
        try{
            List<Content> contents = search(body);
            searchResponseBody.setContents(contents);
        } catch (ServiceException ex){
            log.error("service exception occurred while updating content ", ex);
            return handleServiceException(ex);
        } catch (Exception ex){
            log.error("unknown exception occurred while updating content ", ex);
            return handleUnknownException();
        }
        return handleSuccess(searchResponseBody);
    }

    public List<Content> search(SearchBody body) throws ServiceException{
        Integer pageNumber = (body.getPageNumber() != null && body.getPageNumber() > 0 ? body.getPageNumber() : defaultPageNumber) - 1;
        Integer pageSize = (body.getPageSize() != null ? body.getPageSize() : defaultPageSize);
        String searchString = body.getSearchString();
        CollectionType collectionType = body.getCollectionType();

        CriteriaBuilder criteriaBuilder = entityManager
                .getCriteriaBuilder();
        Metamodel m = entityManager.getMetamodel();
        CriteriaQuery<Content> criteriaQuery = criteriaBuilder
                .createQuery(Content.class);
        Root<Content> fromContent = criteriaQuery.from(Content.class);
        List<Predicate> conditions = new ArrayList();


        if(collectionType != null){
            log.info("adding collection type criteria in the query");
            Join<Content, CollectionContentRelation> ccrJoin = fromContent.join("ccrs", JoinType.INNER);
            Join<CollectionContentRelation, Collection> collectionJoin = ccrJoin.join("collection", JoinType.INNER);
            conditions.add(criteriaBuilder.equal(collectionJoin.get("collectionType"), collectionType));
        }

        CriteriaQuery<Content> select = criteriaQuery.select(fromContent);
        if (searchString != null && !searchString.isEmpty()){
            log.info("adding title criteria in the query");
            conditions.add(criteriaBuilder.like(fromContent.get("title"), "%" + searchString + "%"));
        }

        List<Content> contents = entityManager.createQuery(select.where(conditions.toArray(new Predicate[] {})))
                .setFirstResult(pageNumber)
                .setMaxResults(pageSize)
                .getResultList();

        log.info("content list fetched fromContent db : {}", contents);
        return contents;
    }




}
