package com.jobtracker.application.service;

import com.jobtracker.application.exception.UserAlreadyExistsException;
import com.jobtracker.application.model.request.LoginRequest;
import com.jobtracker.application.model.request.RegisterRequest;

public interface AuthService {

    String login(LoginRequest request) throws UserAlreadyExistsException;

    void register(RegisterRequest request) throws UserAlreadyExistsException;

}
