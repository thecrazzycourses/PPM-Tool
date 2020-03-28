package com.crazzy.rahul.ppmtool.services;

import com.crazzy.rahul.ppmtool.entity.User;
import com.crazzy.rahul.ppmtool.exception.UsernameAlreadyExistException;
import com.crazzy.rahul.ppmtool.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // BCryptPasswordEncoder Bean is required to autowire this, which we created in SecurityConfig
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            // username should be unique

            // make sure password and confirm password are same

            // we dont persist and show password or confirm password
            user.setConfirmPassword("");

            return userRepository.save(user);
        } catch (Exception ex) {
            throw new UsernameAlreadyExistException("User with username : " + user.getUsername() + ", already exist");
        }
    }


}
