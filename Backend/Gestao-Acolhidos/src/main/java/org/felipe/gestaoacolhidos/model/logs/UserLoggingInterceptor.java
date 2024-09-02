package org.felipe.gestaoacolhidos.model.logs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserLoggingInterceptor implements HandlerInterceptor {

    private String registeredUser;

    private static final Logger logger = LoggerFactory.getLogger(UserLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            registerUser(userDetails.getUsername());
            logger.info("User: {} is making a request to {}", userDetails.getUsername(), request.getRequestURI());
        } else {
            logger.info("Anonymous user is making a request to {}", request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        var user = getRegisteredUser();
        if (user != null) {
            logger.info("User: {} made a successful request to {} from {}, status {}",
                    getRegisteredUser(), request.getRequestURI(), request.getRemoteAddr(), response.getStatus());
        }else{
            logger.info("Anonymous user made a successful request to {}, from {}, status {}",
                    request.getRequestURI(), request.getRemoteAddr(), response.getStatus());
        }
    }

    private void registerUser(String username){
        this.registeredUser = username;
    }

    public String getRegisteredUser() {
        return registeredUser;
    }
}
