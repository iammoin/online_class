package com.onlineclass.validator;

import com.onlineclass.exception.ServiceException;
import com.onlineclass.request.CollectionBody;
import org.springframework.stereotype.Service;

@Service
public class CollectionCreatorValidator {

    public void validate(CollectionBody body) throws ServiceException {
        if(body.getCollectionName() == null || body.getCollectionName().isEmpty())
            throw new ServiceException("collection name is mandatory", 400);

        if(body.getCollectionType() == null)
            throw new ServiceException("collection type is mandatory", 400);

    }
}
