package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.exception.ResourceNotFoundException;
import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.repository.AppUserRepository;
import com.bekassyl.ecommerceapp.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final SecureRandom secureRandom = new SecureRandom();

    public Optional<AppUser> getUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Transactional
    public void registerUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole(AppUser.Role.USER);
        appUser.setConfirmationCode(generateConfirmationCode());
        appUser.setEmailConfirmation(false);

        emailService.sendConfirmationCode(appUser);

        try {
            appUserRepository.save(appUser);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists!");
        }
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        AppUser appUser = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found!"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), appUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect!");
        }

        appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        appUserRepository.save(appUser);
    }

    @Transactional
    public void confirmEmail(String email, String confirmationCode) {
        AppUser appUser = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found!"));

        if (appUser.getConfirmationCode().equals(confirmationCode)) {
            appUser.setEmailConfirmation(true);
            appUser.setConfirmationCode(confirmationCode);
            appUserRepository.save(appUser);
        } else {
            System.out.println(appUser.getConfirmationCode());
            System.out.println(confirmationCode);
            System.out.println(email);
            throw new BadCredentialsException("Invalid confirmation code!");
        }
    }

    private String generateConfirmationCode() {
        int code = 100000 + secureRandom.nextInt(900000);

        return String.valueOf(code);
    }
}
