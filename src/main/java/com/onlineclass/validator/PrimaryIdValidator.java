package com.onlineclass.validator;

import org.springframework.stereotype.Service;

@Service
public class PrimaryIdValidator {
    public boolean validate(Integer id){
        return (id != null && id > 0);
    }
}
