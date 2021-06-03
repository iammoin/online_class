package com.onlineclass.repository;

import com.onlineclass.dto.CollectionType;
import com.onlineclass.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ContentRepository extends PagingAndSortingRepository<Content, Integer> {
    String listQuery = "SELECT c2 FROM Collection c1 JOIN CollectionContentRelation ccr ON c1.id = ccr.collectionId " +
            " JOIN Content c2 ON c2.id = ccr.contentId " +
            " WHERE c1.collectionType = :collectionType AND c2.title LIKE '%:title%' ";

    String countQuery = "SELECT COUNT(c2.id) FROM Collection c1 JOIN CollectionContentRelation ccr ON c1.id = ccr.collectionId " +
            " JOIN Content c2 ON c2.id = ccr.contentId " +
            " WHERE c1.collectionType = :collectionType AND c2.title LIKE '%:title%' ";

    @Query("SELECT id FROM Content WHERE id IN (:ids)")
    Set<Integer> findByIdIn(Set<Integer> ids);

    Page<Content> findByTitleContaining(String title, Pageable pageable);

    @Query(value = listQuery, countQuery = countQuery)
    Page<Content> getContentsByCollectionType(CollectionType collectionType, String title, Pageable pageable);
}


