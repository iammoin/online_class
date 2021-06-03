package com.onlineclass.repository;

import com.onlineclass.entity.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionRepository extends CrudRepository<Collection, Integer> {
}
