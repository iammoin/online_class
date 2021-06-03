package com.onlineclass.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineclass.entity.Content;
import com.onlineclass.request.BaseBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResponseBody extends BaseBody {
    private List<Content> contents;
}
