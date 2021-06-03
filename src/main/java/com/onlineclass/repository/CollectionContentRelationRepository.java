package com.onlineclass.repository;

import com.onlineclass.entity.CollectionContentRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CollectionContentRelationRepository extends CrudRepository<CollectionContentRelation, Integer> {

    @Query("SELECT ccr.contentId FROM CollectionContentRelation ccr WHERE ccr.collectionId = :collectionId ")
    public List<Integer> findByCollectionId(Integer collectionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CollectionContentRelation ccr WHERE ccr.collectionId = :collectionId ")
    public void deleteByCollectionId(Integer collectionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CollectionContentRelation ccr WHERE ccr.contentId = :contentId ")
    public void deleteByContentId(Integer contentId);
}
