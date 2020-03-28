package com.crazzy.rahul.ppmtool.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/api/users/**";
    public static final String H2_URL = "h2-console/**";

    public static final String SECRET = "secretKeyToGenerateJwt";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    //public static final Long TOKEN_EXPIRATION_TIME = 30000L; // 30 seconds
    public static final Long TOKEN_EXPIRATION_TIME = 900000L; // 15 minute
}
