package com.onlineclass.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineclass.dto.ContentType;
import com.onlineclass.entity.Content;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentBody extends BaseBody {
    private Integer contentId;
    private String url;
    private String title;
    private ContentType contentType;
    private CollectionBody collectionBody;

    public void update(Content content){
        this.contentId = content.getId();
        this.url = content.getUrl();
        this.title = content.getTitle();
        this.contentType = content.getContentType();
    }
}
