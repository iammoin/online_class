package com.onlineclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineclass.dto.ContentType;
import com.onlineclass.request.ContentBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastUpdatedDate;

    @OneToMany
    @JoinColumn(name="contentId", referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    private List<CollectionContentRelation> ccrs;

    public Content(ContentBody body) {
        this.title = body.getTitle();
        this.url = body.getUrl();
        this.contentType = body.getContentType();
    }

    public Content(Integer id, String title, String url, ContentType contentType) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.contentType = contentType;
    }

    public void updateNotNullProperties(ContentBody body){
        if(body.getContentType() != null)
            this.contentType = body.getContentType();
        if(body.getTitle() != null && !body.getTitle().isEmpty())
            this.title = body.getTitle();
        if(body.getUrl() != null && !body.getUrl().isEmpty())
            this.url = body.getUrl();
    }
}
