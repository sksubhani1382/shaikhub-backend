package com.shaikhub.controller;

import com.shaikhub.model.User;
import com.shaikhub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // -------- Admin Login Page --------
    @GetMapping("/login")
    public String adminLogin() {
        return "admin-login";
    }

    // -------- Login Submit --------
    @PostMapping("/login")
    public String adminLoginSubmit(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            session.setAttribute("ADMIN", true);
            return "redirect:/admin/users";
        }

        model.addAttribute("error", "Invalid admin credentials");
        return "admin-login";
    }

    // -------- View Users Page --------
    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model) {
        if (session.getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "admin-users";
    }

    // -------- Delete User --------
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {

        if (session.getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        userRepo.deleteById(id);

        return "redirect:/admin/users";
    }

    // -------- Logout --------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
