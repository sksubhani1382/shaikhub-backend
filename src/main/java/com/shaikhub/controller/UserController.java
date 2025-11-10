package com.shaikhub.controller;

import com.shaikhub.model.User;
import com.shaikhub.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepository userRepo;

    @GetMapping("/profile")
    public Object profile(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) return Map.of("error", "Unauthorized");
        User u = userRepo.findByEmail(email);
        return Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail(), "mobile", u.getMobile());
    }
}