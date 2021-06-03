package com.onlineclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CollectionContentRelation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer collectionId;

    private Integer contentId;

    public CollectionContentRelation(Integer collectionId, Integer contentId) {
        this.collectionId = collectionId;
        this.contentId = contentId;
    }

    @ManyToOne
    @JoinColumn(name="collectionId",referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    @ToString.Exclude
    private Collection collection;

    @ManyToOne
    @JoinColumn(name="contentId",referencedColumnName="id", insertable=false, updatable=false)
    @JsonIgnore
    @ToString.Exclude
    private Content content;
}
