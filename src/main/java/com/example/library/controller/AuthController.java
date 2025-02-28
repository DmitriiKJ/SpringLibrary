package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            response.put("success", false);
            response.put("message", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpServletResponse response){
        Map<String, Object> responseBody = new HashMap<>();
        Optional<User> foundUser = userRepository.findUserByUsername(user.getUsername());
        if(foundUser.isEmpty() || !passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())){
            responseBody.put("success", false);
            responseBody.put("message", "Username or password are incorrect");
            return ResponseEntity.badRequest().body(responseBody);
        }

        String token = jwtService.generateJwtToken(foundUser.get());

        // Create Cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        // Add to response
        response.addCookie(cookie);

        responseBody.put("success", true);
        responseBody.put("message", "Login successful");
        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
    }
}
