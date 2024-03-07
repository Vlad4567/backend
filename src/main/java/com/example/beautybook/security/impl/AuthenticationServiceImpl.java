package com.example.beautybook.security.impl;

import com.example.beautybook.dto.user.RequestRefreshDto;
import com.example.beautybook.dto.user.ResponseRefreshDto;
import com.example.beautybook.dto.user.UserLoginRequestDto;
import com.example.beautybook.dto.user.UserLoginResponseDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.LoginException;
import com.example.beautybook.exceptions.UnverifiedUserException;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.user.RoleRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.security.AuthenticationService;
import com.example.beautybook.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserLoginResponseDto authentication(UserLoginRequestDto loginRequestDto) {
        try {
            final Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDto.getEmail(),
                                    loginRequestDto.getPassword()
                            )
                    );
            checkUserVerificationStatus(authentication.getName());
            String token = jwtUtil.generateToken(authentication.getName(), JwtUtil.Secret.AUTH);
            if (loginRequestDto.isRemember()) {
                String refreshToken =
                        jwtUtil.generateToken(authentication.getName(), JwtUtil.Secret.REFRESH);
                saveRefreshToken(authentication.getName(), refreshToken);
                return new UserLoginResponseDto(token, refreshToken);
            }
            return new UserLoginResponseDto(token, null);
        } catch (AuthenticationException e) {
            throw new LoginException(e.getLocalizedMessage());
        }
    }

    @Override
    public boolean verificationEmail(String token) {
        if (!jwtUtil.isValidToken(token, JwtUtil.Secret.MAIL)) {
            return false;
        }
        String username = jwtUtil.getUsername(token, JwtUtil.Secret.MAIL);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by email: " + username
                ));
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName(Role.RoleName.CUSTOMER)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Not found role by name: " + Role.RoleName.CUSTOMER)));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @Override
    public ResponseRefreshDto refreshToken(RequestRefreshDto requestRefreshDto) {
        String refreshToken = requestRefreshDto.refreshToken();
        String email =
                jwtUtil.getUsername(refreshToken, JwtUtil.Secret.REFRESH);
        String token = userRepository.getRefreshTokenByEmail(email);
        if (token == null
                || !jwtUtil.isValidToken(refreshToken, JwtUtil.Secret.REFRESH)
                || !token.equals(refreshToken)) {
            throw new JwtException("RefreshToken in not valid");
        }
        return new ResponseRefreshDto(jwtUtil.generateToken(email, JwtUtil.Secret.AUTH));
    }

    @Override
    public void deleteRefreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Not found user by email "
                        + authentication.getName()));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    private void checkUserVerificationStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by email: " + email)
                );
        Optional<Role.RoleName> roleName = user.getRoles().stream()
                .map(Role::getName)
                .filter(name -> name.equals(Role.RoleName.UNVERIFIED))
                .findFirst();
        if (roleName.isPresent()) {
            throw new UnverifiedUserException("User's email is not confirmed");
        }
    }

    private void saveRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Not found user by email: " + email));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
