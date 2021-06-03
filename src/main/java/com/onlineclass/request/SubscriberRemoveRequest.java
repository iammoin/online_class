package com.onlineclass.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscriberRemoveRequest {
    private String subscriberId;
    private String queueName;
}
