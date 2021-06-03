package com.onlineclass.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineclass.dto.CollectionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchBody extends BaseBody {
    private Integer pageNumber;
    private Integer pageSize;
    private String searchString;
    private CollectionType collectionType;
}
