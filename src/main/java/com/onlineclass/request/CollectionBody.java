package com.onlineclass.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineclass.dto.CollectionType;
import com.onlineclass.entity.Collection;
import com.onlineclass.entity.Content;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;


@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionBody extends BaseBody {
    private Integer collectionId;
    private String collectionName;
    private CollectionType collectionType;
    private Set<Integer> contentIds;

    public void update(Collection collection){
        this.collectionId = collection.getId();
        this.collectionName = collection.getName();
        this.collectionType = collection.getCollectionType();
    }
}
