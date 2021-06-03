package com.onlineclass.service.impl;

import com.onlineclass.dto.CollectionType;
import com.onlineclass.dto.ContentType;
import com.onlineclass.dto.UserType;
import com.onlineclass.entity.Collection;
import com.onlineclass.entity.Content;
import com.onlineclass.exception.ServiceException;
import com.onlineclass.repository.ContentRepository;
import com.onlineclass.request.BaseRequest;
import com.onlineclass.request.CollectionBody;
import com.onlineclass.request.ContentBody;
import com.onlineclass.response.BaseResponse;
import com.onlineclass.validator.ContentCreatorValidator;
import com.onlineclass.validator.PrimaryIdValidator;
import com.onlineclass.validator.UserTypeValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;


class ContentServiceImplTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private PrimaryIdValidator primaryIdValidator;

    @Mock
    private UserTypeValidator userTypeValidator;

    @Mock
    private ContentCreatorValidator contentCreatorValidator;

    @Mock
    private CollectionServiceImpl collectionService;

    @Mock
    private CollectionContentServiceImpl collectionContentService;


    private Integer defaultPageNumber;

    private Integer defaultPageSize;

    @Mock
    private EntityManager entityManager;


    @InjectMocks
    private ContentServiceImpl cs = new ContentServiceImpl();

    @BeforeEach
    public void setup() throws ServiceException {
        MockitoAnnotations.initMocks(this);
        defaultPageNumber = 1;
        defaultPageSize = 10;
        ReflectionTestUtils.setField(cs, "defaultPageNumber", defaultPageNumber);
        ReflectionTestUtils.setField(cs, "defaultPageSize", defaultPageSize);

        Content content = new Content(1, "title 1", "url 1", ContentType.VIDEO);
        Collection collection = new Collection(101, "tag 1", CollectionType.TAG);
        Optional<Content> optionalEmptyContent = Optional.empty();
        Optional<Content> optionalContent = Optional.of(content);
        Mockito.doNothing()
                .when(userTypeValidator).validate(Mockito.any(UserType.class), Mockito.any(UserType.class));
        Mockito.doNothing()
                .when(contentCreatorValidator).validate(Mockito.any());
        Mockito.doReturn(content)
                .when(contentRepository).save(Mockito.any());
        Mockito.doReturn(collection)
                .when(collectionService).create(Mockito.any(CollectionBody.class));
        Mockito.doReturn(true)
                .when(primaryIdValidator).validate(Mockito.any());
        Mockito.doReturn(false)
                .when(primaryIdValidator).validate(null);
        Mockito.doReturn(optionalContent)
                .when(contentRepository).findById(1);
        Mockito.doReturn(optionalEmptyContent)
                .when(contentRepository).findById(2);

    }

    @Test
    void createWithNullUserType() throws ServiceException {
        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        Mockito.doThrow(new ServiceException(UserTypeValidator.INVALID_USER_TYPE, 400))
                .when(userTypeValidator).validate(baseRequest.getUserType(), UserType.INSTRUCTOR);

        BaseResponse baseResponse = cs.create(baseRequest);

        Assert.assertEquals(UserTypeValidator.INVALID_USER_TYPE, baseResponse.getMessage());
        Assert.assertEquals(400, baseResponse.getMessageCode());
    }

    @Test
    void createWithStudentUserType() throws ServiceException {
        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.STUDENT);

        Mockito.doThrow(new ServiceException(UserTypeValidator.ACTION_NOT_ALLOWED_FOR_USER, 403))
                .when(userTypeValidator).validate(baseRequest.getUserType(), UserType.INSTRUCTOR);

        BaseResponse baseResponse = cs.create(baseRequest);

        Assert.assertEquals(UserTypeValidator.ACTION_NOT_ALLOWED_FOR_USER, baseResponse.getMessage());
        Assert.assertEquals(403, baseResponse.getMessageCode());
    }

    @Test
    void createWithNullorEmptyUrl() throws ServiceException {
        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        ContentBody contentBody = new ContentBody();

        Mockito.doNothing()
                .when(userTypeValidator).validate(Mockito.any(UserType.class), Mockito.any(UserType.class));
        Mockito.doThrow(new ServiceException(ContentCreatorValidator.CONTENT_URL_IS_MANDATORY, 400))
                .when(contentCreatorValidator).validate(baseRequest.getBody());

        BaseResponse baseResponse = cs.create(baseRequest);
        Assert.assertEquals(ContentCreatorValidator.CONTENT_URL_IS_MANDATORY, baseResponse.getMessage());
        Assert.assertEquals(400, baseResponse.getMessageCode());

        contentBody.setUrl("");

        baseResponse = cs.create(baseRequest);
        Assert.assertEquals(ContentCreatorValidator.CONTENT_URL_IS_MANDATORY, baseResponse.getMessage());
        Assert.assertEquals(400, baseResponse.getMessageCode());
    }

    @Test
    void createVideoWithSuccessResponse() throws ServiceException {
        ContentBody contentBody = new ContentBody();
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.VIDEO);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);


        BaseResponse<ContentBody> baseResponse = cs.create(baseRequest);
        ContentBody responseBody = baseResponse.getBody();

        Assert.assertEquals(200, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.SUCCESS, baseResponse.getMessage());

        Assert.assertEquals(1, responseBody.getContentId().intValue());
        Assert.assertEquals(contentBody.getUrl(), responseBody.getUrl());
        Assert.assertEquals(contentBody.getTitle(), responseBody.getTitle());
        Assert.assertEquals(contentBody.getContentType(), responseBody.getContentType());

    }

    @Test
    void createLessonWithSuccessResponse() throws ServiceException {
        ContentBody contentBody = new ContentBody();
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.LESSON);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);


        BaseResponse<ContentBody> baseResponse = cs.create(baseRequest);
        ContentBody responseBody = baseResponse.getBody();

        Assert.assertEquals(200, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.SUCCESS, baseResponse.getMessage());

        Assert.assertEquals(1, responseBody.getContentId().intValue());
        Assert.assertEquals(contentBody.getUrl(), responseBody.getUrl());
        Assert.assertEquals(contentBody.getTitle(), responseBody.getTitle());
        Assert.assertEquals(contentBody.getContentType(), responseBody.getContentType());

    }

    @Test
    void createWebinarWithSuccessResponse() throws ServiceException {
        ContentBody contentBody = new ContentBody();
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.WEBINAR);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);


        BaseResponse<ContentBody> baseResponse = cs.create(baseRequest);
        ContentBody responseBody = baseResponse.getBody();

        Assert.assertEquals(200, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.SUCCESS, baseResponse.getMessage());

        Assert.assertEquals(1, responseBody.getContentId().intValue());
        Assert.assertEquals(contentBody.getUrl(), responseBody.getUrl());
        Assert.assertEquals(contentBody.getTitle(), responseBody.getTitle());
        Assert.assertEquals(contentBody.getContentType(), responseBody.getContentType());

    }

    @Test
    void createTagAlongWithVideo() throws ServiceException {

        CollectionBody collectionBody = new CollectionBody();
        collectionBody.setCollectionName("tag 1");
        collectionBody.setCollectionType(CollectionType.TAG);

        ContentBody contentBody = new ContentBody();
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.WEBINAR);
        contentBody.setCollectionBody(collectionBody);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);


        BaseResponse<ContentBody> baseResponse = cs.create(baseRequest);
        ContentBody responseBody = baseResponse.getBody();

        Assert.assertEquals(200, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.SUCCESS, baseResponse.getMessage());

        Assert.assertEquals(1, responseBody.getContentId().intValue());
        Assert.assertEquals(contentBody.getUrl(), responseBody.getUrl());
        Assert.assertEquals(contentBody.getTitle(), responseBody.getTitle());
        Assert.assertEquals(contentBody.getContentType(), responseBody.getContentType());
        Assert.assertEquals(101, contentBody.getCollectionBody().getCollectionId().intValue());
        Mockito.verify(collectionContentService, Mockito.times(1)).update(101, new HashSet<>(Arrays.asList(1)));

    }

    @Test
    void updateContentWithoutContentId() {
        ContentBody contentBody = new ContentBody();
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.VIDEO);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);

        BaseResponse<ContentBody> baseResponse = cs.update(baseRequest);

        Assert.assertEquals(400, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.INVALID_CONTENT_ID, baseResponse.getMessage());
        Assert.assertEquals(null, baseResponse.getBody());

    }

    @Test
    void updateContentWithInvalidContentId(){
        ContentBody contentBody = new ContentBody();
        contentBody.setContentId(2);
        contentBody.setUrl("url 1");
        contentBody.setTitle("title 1");
        contentBody.setContentType(ContentType.VIDEO);

        BaseRequest<ContentBody> baseRequest = new BaseRequest<>();
        baseRequest.setUserType(UserType.INSTRUCTOR);
        baseRequest.setBody(contentBody);


        BaseResponse<ContentBody> baseResponse = cs.update(baseRequest);

        Assert.assertEquals(404, baseResponse.getMessageCode());
        Assert.assertEquals(AbstractService.CONTENT_DOES_NOT_EXIST_FOR_CONTENT_ID + contentBody.getContentId(), baseResponse.getMessage());
        Assert.assertEquals(null, baseResponse.getBody());

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void testCreate() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void search() {
    }

    @Test
    void testSearch() {
    }
}