package com.crazzy.rahul.ppmtool.services;

import com.crazzy.rahul.ppmtool.entity.User;
import com.crazzy.rahul.ppmtool.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // This return object for type UserDetail that's why we implement UserDetail in User Entity
    // Ideally we should use different class for implementing UserDetail and extend that to User Entity
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username : " + username + ", not found!");
        }

        return user;
    }

    @Transactional
    public User loadUserById(Long id) {
        User user = userRepository.getById(id);

        if (user == null) {
            throw new UsernameNotFoundException("User with id : " + id + ", not found!");
        }

        return user;
    }
}
