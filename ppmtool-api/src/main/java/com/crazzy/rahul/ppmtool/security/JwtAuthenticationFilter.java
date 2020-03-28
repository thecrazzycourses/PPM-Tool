package com.crazzy.rahul.ppmtool.security;

import com.crazzy.rahul.ppmtool.entity.User;
import com.crazzy.rahul.ppmtool.services.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.crazzy.rahul.ppmtool.security.SecurityConstants.HEADER_STRING;
import static com.crazzy.rahul.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            String jwt = getJwtTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                Long userIdFromJwt = jwtTokenProvider.getUserIdFromJwt(jwt);
                User user = customUserDetailService.loadUserById(userIdFromJwt);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception ex) {
            log.info("JwtAuthenticationFilter Exception");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER_STRING);

        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith(TOKEN_PREFIX)) {
            return  jwtToken.substring(7, jwtToken.length());
        }

        return null;
    }
}
