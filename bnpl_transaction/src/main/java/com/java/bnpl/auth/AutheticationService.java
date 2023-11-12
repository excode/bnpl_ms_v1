package com.java.bnpl.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.bnpl.config.JwtService;
import com.java.bnpl.user.Role;
import com.java.bnpl.user.User;
import com.java.bnpl.user.UserRepository;
import com.java.bnpl.users.UsersRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AutheticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
   
     private final  UsersRepository usersRepository;
    public AuthenticationResponse register(RegisterRequest request) {
        var userCheck = userRepository.findByEmail(request.getEmail());
        if(userCheck.isPresent()){
            throw new IllegalStateException("email_taken");
        }
        var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .mobile(request.getMobile())
        .userType(1)
        .role(Role.User)
        .build();
        userRepository.save(user);
       var users =this.usersRepository.findUsersByEmail(request.getEmail()).orElse(null);
        var jwtToken = jwtService.generateToken(users);
        return AuthenticationResponse
        .builder()
        .accessToken(jwtToken).build();
    }

public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword())
     );
     var user = userRepository.findByEmail(request.getEmail())
     .orElseThrow();
       var users =this.usersRepository.findUsersByEmail(request.getEmail()).orElse(null);
     var jwtToken = jwtService.generateToken(users);
        return AuthenticationResponse
        .builder()
        .accessToken(jwtToken).build();
}

    
}