package com.shaikhub.controller;

import com.shaikhub.model.User;
import com.shaikhub.repo.UserRepository;
import com.shaikhub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        if (userRepo.findByEmail(body.get("email")) != null)
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));

        String hashed = BCrypt.hashpw(body.get("password"), BCrypt.gensalt());
        User u = new User(body.get("name"), body.get("email"), body.get("mobile"), hashed);
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        User u = userRepo.findByEmail(body.get("email"));
        if (u == null || !BCrypt.checkpw(body.get("password"), u.getPassword()))
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));

        String token = jwtUtil.generateToken(u.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "name", u.getName(), "email", u.getEmail()));
    }
}