package com.learnifier.recruitment.repository;

import com.learnifier.recruitment.model.Metadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, Long> {
}
