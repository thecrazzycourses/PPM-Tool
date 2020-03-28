package com.crazzy.rahul.ppmtool.repositories;

import com.crazzy.rahul.ppmtool.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);
}
