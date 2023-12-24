package org.example.services;

import org.example.dao.requests.SignInRequest;
import org.example.dao.requests.SignUpRequest;
import org.example.dao.responses.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
