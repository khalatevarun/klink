package com.url.url_shortner_sb.service;

import com.url.url_shortner_sb.dtos.LoginRequest;
import com.url.url_shortner_sb.models.User;
import com.url.url_shortner_sb.repository.UserRepository;
import com.url.url_shortner_sb.security.jwt.JwtAuthenticationResponse;
import com.url.url_shortner_sb.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                           loginRequest.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
           String jwt = jwtUtils.generateToken(userDetails);

           return new JwtAuthenticationResponse(jwt);

    }

}
