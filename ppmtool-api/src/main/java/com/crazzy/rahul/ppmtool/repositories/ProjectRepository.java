package com.crazzy.rahul.ppmtool.repositories;

import com.crazzy.rahul.ppmtool.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {

   Project findByProjectIdentifier(String projectIdentifier);
   Iterable<Project> findAllByProjectLeader(String username);

   Project findByProjectIdentifierAndProjectLeader(String projectIdentifier, String username);
}
