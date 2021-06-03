package com.onlineclass.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineclass.request.BaseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T extends BaseBody>{
    private String message;
    private int messageCode;
    private T body;
}
