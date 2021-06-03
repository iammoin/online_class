package com.onlineclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineclass.dto.CollectionType;
import com.onlineclass.request.CollectionBody;
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
public class Collection {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollectionType collectionType;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastUpdatedDate;

    @OneToMany
    @JoinColumn(name="collectionId",referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    private List<CollectionContentRelation> ccrs;

    public Collection(CollectionBody body) {
        this.name = body.getCollectionName();
        this.collectionType = body.getCollectionType();
    }

    public Collection(Integer id, String name, CollectionType collectionType) {
        this.id = id;
        this.name = name;
        this.collectionType = collectionType;
    }

    public void updateNotNullProperties(CollectionBody body){
        if(body.getCollectionName() != null && !body.getCollectionName().isEmpty())
            this.name = body.getCollectionName();
        if(body.getCollectionType() != null)
            this.collectionType = body.getCollectionType();
    }

}
