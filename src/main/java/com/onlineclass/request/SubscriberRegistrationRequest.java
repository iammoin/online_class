package com.onlineclass.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SubscriberRegistrationRequest {
    private String subscriberId;
    private String callbackUrl;
    private int batchSize;
    private String queueName;
}
