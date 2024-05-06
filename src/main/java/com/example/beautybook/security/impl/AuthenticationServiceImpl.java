package com.example.beautybook.security.impl;

import com.example.beautybook.dto.TelegramLoginDto;
import com.example.beautybook.dto.user.request.RequestRefreshDto;
import com.example.beautybook.dto.user.request.UserLoginRequestDto;
import com.example.beautybook.dto.user.response.ResponseRefreshDto;
import com.example.beautybook.dto.user.response.UserLoginResponseDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.LoginDeviceLimitExceededException;
import com.example.beautybook.exceptions.LoginException;
import com.example.beautybook.exceptions.UnverifiedUserException;
import com.example.beautybook.model.AuthenticationFailureLog;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.AuthenticationFailureLogRepository;
import com.example.beautybook.repository.TelegramCodeRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.security.AuthenticationService;
import com.example.beautybook.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOG_EXPIRATION_MINUTES = 3;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthenticationFailureLogRepository failureLogRepository;
    private final ScheduledExecutorService executor;
    private final TelegramCodeRepository codeRepository;

    @Override
    @Transactional()
    public UserLoginResponseDto authentication(UserLoginRequestDto loginRequestDto) {
        String uuid = userRepository.findUserByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new LoginException("Bad credentials")).getUuid();
        try {
            final Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    uuid,
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
            throw new LoginException("Bad credentials");
        }
    }

    @Override
    @Transactional
    public boolean verificationEmail(String token) {
        if (!jwtUtil.isValidToken(token, JwtUtil.Secret.MAIL)) {
            return false;
        }
        String username = jwtUtil.getUsername(token, JwtUtil.Secret.MAIL);
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by Email: " + username
                ));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(4L));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public ResponseRefreshDto refreshToken(RequestRefreshDto requestRefreshDto) {
        String refreshToken = requestRefreshDto.refreshToken();
        String uuid =
                jwtUtil.getUsername(refreshToken, JwtUtil.Secret.REFRESH);
        String token = userRepository.getRefreshTokenByUuid(uuid);
        if (token == null
                || !jwtUtil.isValidToken(refreshToken, JwtUtil.Secret.REFRESH)
                || !token.equals(refreshToken)) {
            throw new JwtException("RefreshToken in not valid");
        }
        return new ResponseRefreshDto(jwtUtil.generateToken(uuid, JwtUtil.Secret.AUTH));
    }

    @Override
    @Transactional
    public void deleteRefreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUuid(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Not found user by email "
                        + authentication.getName()));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserLoginResponseDto loginWithTelegram(TelegramLoginDto dto) {
        AuthenticationFailureLog failureLog = failureLogRepository.findByToken(dto.token())
                .orElseThrow(() -> new LoginException("Authorization session not found."));
        if (failureLog.getBlockEndTime() != null
                && failureLog.getBlockEndTime().isAfter(LocalDateTime.now())) {
            long seconds = Duration.between(
                    failureLog.getBlockEndTime(), LocalDateTime.now()).getSeconds();
            throw new LoginDeviceLimitExceededException(
                    "The limit on invalid entries has been exceeded. "
                            + "Try again in" + seconds + "seconds");
        }
        if (!codeRepository.existsByCode(dto.code())) {
            if (checkAndAddToBlock(failureLog)) {
                throw new LoginDeviceLimitExceededException(
                        "The limit on invalid entries has been exceeded. "
                                + "Try again in 30 seconds");
            } else {
                throw new LoginException("Code is invalid");
            }
        }
        failureLogRepository.deleteById(failureLog.getId());
        String authToken = jwtUtil.generateToken(
                codeRepository.findById(dto.code()).get().getUserUuid(), JwtUtil.Secret.AUTH);
        return new UserLoginResponseDto(authToken, null);
    }

    @Override
    @Transactional
    public String getLoginDeviceToken(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        int count = failureLogRepository.countLogsAddedSinceByIp(
                LocalDateTime.now().minusMinutes(1L), ip);
        if (count >= MAX_LOGIN_ATTEMPTS) {
            throw new LoginDeviceLimitExceededException("The maximum number of tables allowed "
                    + "for entry with this device was exceeded at the last minute.");
        }
        String token = jwtUtil.generateToken(UUID.randomUUID().toString(), JwtUtil.Secret.AUTH);
        AuthenticationFailureLog failureLog = failureLogRepository.save(
                new AuthenticationFailureLog(ip, token, LocalDateTime.now()));

        executor.schedule(
                () -> failureLogRepository.deleteById(failureLog.getId()),
                LOG_EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );
        return token;
    }

    private void checkUserVerificationStatus(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by uuid: " + uuid)
                );
        Optional<Role.RoleName> roleName = user.getRoles().stream()
                .map(Role::getName)
                .filter(name -> name.equals(Role.RoleName.UNVERIFIED))
                .findFirst();
        if (roleName.isPresent()) {
            throw new UnverifiedUserException("User's email is not confirmed");
        }
    }

    private void saveRefreshToken(String uuid, String refreshToken) {
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new EntityNotFoundException("Not found user by uuid: " + uuid));
        user.setRefreshToken(refreshToken);
        User newuser = userRepository.save(user);

    }

    private boolean checkAndAddToBlock(AuthenticationFailureLog failureLog) {
        int loginFails = failureLog.getLoginFails() + 1;
        if (loginFails >= MAX_LOGIN_ATTEMPTS) {
            failureLog.setLoginFails(0);
            failureLog.setBlockEndTime(LocalDateTime.now().plusSeconds(30));
            failureLogRepository.save(failureLog);
            return true;
        }
        failureLog.setLoginFails(loginFails);
        failureLogRepository.save(failureLog);
        return false;
    }
}

