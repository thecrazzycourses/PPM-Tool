package com.crazzy.rahul.ppmtool.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAlreadyExistResponse {

    private String username;

    public UsernameAlreadyExistResponse(String username) {
        this.username = username;
    }
}
