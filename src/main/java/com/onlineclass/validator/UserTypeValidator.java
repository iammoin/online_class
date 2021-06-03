package com.onlineclass.validator;

import com.onlineclass.dto.UserType;
import com.onlineclass.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class UserTypeValidator {
    public static final String INVALID_USER_TYPE = "invalid user type";
    public static final String ACTION_NOT_ALLOWED_FOR_USER = "action not allowed for user type : ";
    public void validate(UserType requestUser, UserType requiredUser) throws ServiceException {
        if(requestUser == null)
            throw new ServiceException(INVALID_USER_TYPE, 400);

        if(!requestUser.equals(requiredUser))
            throw new ServiceException(ACTION_NOT_ALLOWED_FOR_USER + requestUser, 403);
    }
}
