package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new AppUserDetails(appUser);
    }
}
