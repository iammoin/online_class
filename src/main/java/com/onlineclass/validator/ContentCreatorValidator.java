package com.onlineclass.validator;

import com.onlineclass.exception.ServiceException;
import com.onlineclass.request.CollectionBody;
import com.onlineclass.request.ContentBody;
import org.springframework.stereotype.Service;

@Service
public class ContentCreatorValidator {

    public static final String CONTENT_URL_IS_MANDATORY = "content url is mandatory";
    public static final String CONTENT_TITLE_IS_MANDATORY = "content title is mandatory";
    public static final String CONTENT_TYPE_IS_MANDATORY = "content type is mandatory";

    public void validate(ContentBody body) throws ServiceException {
        if(body.getUrl() == null || body.getUrl().isEmpty())
            throw new ServiceException(CONTENT_URL_IS_MANDATORY, 400);

        if(body.getTitle() == null || body.getTitle().isEmpty())
            throw new ServiceException(CONTENT_TITLE_IS_MANDATORY, 400);

        if(body.getContentType() == null)
            throw new ServiceException(CONTENT_TYPE_IS_MANDATORY, 400);

    }
}