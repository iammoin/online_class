package com.onlineclass.service.impl;

import com.onlineclass.exception.ServiceException;
import com.onlineclass.request.BaseBody;
import com.onlineclass.response.BaseResponse;

public class AbstractService {
    public static final String UNKNOWN_EXCEPTION = "unknown exception occurred";
    public static final String SUCCESS = "success";
    public static final String INVALID_CONTENT_ID = "invalid content id";
    public static final String CONTENT_DOES_NOT_EXIST_FOR_CONTENT_ID = "content does not exist for content id ";

    public BaseResponse handleSuccess(BaseBody body){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(SUCCESS);
        baseResponse.setMessageCode(200);
        baseResponse.setBody(body);
        return baseResponse;
    }

    public BaseResponse handleServiceException(ServiceException ex){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(ex.getMessage());
        baseResponse.setMessageCode(ex.getErrorCode());
        return baseResponse;
    }

    public BaseResponse handleUnknownException(){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(UNKNOWN_EXCEPTION);
        baseResponse.setMessageCode(500);
        return baseResponse;
    }

    public BaseResponse handleRuntimeException(String message, Integer messageCode){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(message);
        baseResponse.setMessageCode(messageCode);
        return baseResponse;
    }


}
