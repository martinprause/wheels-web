package com.doit.wheels.auth;

import com.doit.wheels.utils.enums.UserRoleEnum;
import com.vaadin.server.VaadinService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class SecurityUtils {

    private static final String COOKIE_NAME = "remember-me";

    private SecurityUtils() {
    }

    public static boolean isLoggedIn(UserDetailsService userDetailsService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() || loginRememberedUser(userDetailsService);
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

    private static boolean loginRememberedUser(UserDetailsService userDetailsService) {
        Optional<Cookie> rememberMeCookie = getRememberMeCookie();

        if (rememberMeCookie.isPresent()) {
            String username = rememberMeCookie.get().getValue();
            if (username != null && !username.equals("")) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(username), null, userDetailsService.loadUserByUsername(username).getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
                return true;
            }
        }

        return false;
    }

    private static Optional<Cookie> getRememberMeCookie() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        return Arrays.stream(cookies).filter(c -> c.getName().equals(COOKIE_NAME)).findFirst();
    }

}
