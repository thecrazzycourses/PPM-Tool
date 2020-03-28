package com.crazzy.rahul.ppmtool.repositories;

import com.crazzy.rahul.ppmtool.entity.Backlog;
import org.springframework.data.repository.CrudRepository;

public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String projectIdentifier);

}
