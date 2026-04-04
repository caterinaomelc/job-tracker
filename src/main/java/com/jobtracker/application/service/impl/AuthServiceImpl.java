package com.jobtracker.application.service.impl;

import com.jobtracker.application.exception.InvalidDataException;
import com.jobtracker.application.exception.UserAlreadyExistsException;
import com.jobtracker.application.exception.UserNotFoundException;
import com.jobtracker.application.model.entity.User;
import com.jobtracker.application.model.request.LoginRequest;
import com.jobtracker.application.model.request.RegisterRequest;
import com.jobtracker.application.repositories.UserRepository;
import com.jobtracker.application.security.JwtUtils;
import com.jobtracker.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequest request) throws UserAlreadyExistsException {


        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidDataException("Passwords don't match");
        }
        return jwtUtils.generateJwtToken(user.getUsername());
    }

    @Override
    public void register(RegisterRequest request) throws UserAlreadyExistsException {

        if (userRepository.findByEmail(request.email()).isPresent()
                || userRepository.findByUsername(request.username()).isPresent()) {
            throw new InvalidDataException("Username or password is invalid");
        }

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setUsername(request.username());
        userRepository.save(newUser);


    }
}
