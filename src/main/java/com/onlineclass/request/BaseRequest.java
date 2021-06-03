package com.onlineclass.request;

import com.onlineclass.dto.UserType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BaseRequest<T extends BaseBody>{
    private UserType userType;
    private T body;
}
