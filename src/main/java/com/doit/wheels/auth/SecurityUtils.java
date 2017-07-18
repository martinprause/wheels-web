package com.doit.wheels.auth;

import com.doit.wheels.utils.enums.UserRoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;


public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(Set<UserRoleEnum> roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            for (UserRoleEnum role : roles) {
                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.name()))){
                    return true;
                }
            }
        }
        return false;
    }
}
